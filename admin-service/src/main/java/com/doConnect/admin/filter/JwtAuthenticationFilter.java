package com.doConnect.admin.filter;

import com.doConnect.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        System.out.println("🔥 JWT Filter - Path: " + request.getRequestURI());
        System.out.println("🔥 JWT Filter - AuthHeader: " + request.getHeader("Authorization"));
        
        // SKIP LOGIN/REGISTER + ALL BUTTON PATHS (ALREADY PERFECT!)
        String path = request.getRequestURI();
        if (path.equals("/admin/login") || 
            path.equals("/admin/register") ||
            path.equals("/login") ||
            path.startsWith("/admin/users/block/") ||      // Block buttons
            path.startsWith("/admin/users/unblock/") ||    // Unblock buttons  
           
            path.startsWith("/css/") ||                    // CSS files
            path.startsWith("/js/")) {                     // JS files
            
            System.out.println("🔥 Skipping JWT check for public paths");
            filterChain.doFilter(request, response);  // 
            return;
        }

        String username = null;
        String jwt = null;

        //  CHECK AUTHORIZATION HEADER FIRST
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔥 Auth header received: " + authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("🔥 JWT extracted: " + jwt.substring(0, 20) + "...");
        } 
        //  THEN CHECK COOKIES (for browser)
        else {
            jakarta.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (jakarta.servlet.http.Cookie cookie : cookies) {
                    if ("jwt-token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        System.out.println("🔥 JWT from cookie: " + jwt.substring(0, 20) + "...");
                        break;
                    }
                }
            }
        }

        // VALIDATE JWT
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("🔥 Validating JWT...");
            username = jwtUtil.extractUsername(jwt);
            System.out.println("🔥 Username from JWT: " + username);
            
            if (username != null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("🔥 UserDetails loaded: " + userDetails.getUsername());
                    
                    if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                        System.out.println("✅ JWT VALID - Setting auth context");
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        System.out.println("❌ JWT validation failed");
                    }
                } catch (Exception e) {
                    System.out.println("❌ UserDetailsService error: " + e.getMessage());
                }
            } else {
                System.out.println("❌ No username in JWT");
            }
        } else {
            System.out.println("❌ No JWT found or already authenticated");
        }
        
        filterChain.doFilter(request, response);
    }

}
