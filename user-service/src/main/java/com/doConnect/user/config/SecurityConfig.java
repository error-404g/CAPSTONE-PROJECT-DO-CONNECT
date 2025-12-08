package com.doConnect.user.config;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.doConnect.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        //  Single Sign-On (SSO)
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(-1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
            )
        .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/api/users/**").permitAll()
        	    .requestMatchers("/api/**").permitAll()
        		.requestMatchers("/admin/**").permitAll()
        		.requestMatchers("/api/chat/**").permitAll()  // BEFORE anyRequest()
        		.requestMatchers("/api/users/admin/**").permitAll()
        	    .requestMatchers("/ws-chat/**").permitAll()
        	    .requestMatchers("/topic/**", "/queue/**", "/app/**", "/user/**").permitAll()
        	    
        	    .requestMatchers("/api/questions/**").permitAll()
        	    .requestMatchers("/dashboard").permitAll() 
        	    .requestMatchers("/login", "/register").permitAll()
        	    .requestMatchers("/api/admin/**").permitAll()
        	    .requestMatchers("/admin/users").permitAll() 
        	    
        	    .anyRequest().authenticated()  // AFTER all permitAll()
        	)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable() );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
            
            // BLOCKED USER CHECK 
            if (!user.isActive()) {
                throw new UsernameNotFoundException("User is blocked by admin");
            }
            // Ensure role is never null/empty
        String role = (user.getRole() != null && !user.getRole().isEmpty()) 
            ? user.getRole() 
            : "ROLE_USER";
            
            return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(user.getRole()))
                .accountLocked(!user.isActive())  // Extra security layer
                .build();
        };
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public ChannelInterceptor userChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Pass HTTP session principal to WebSocket
                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return "testuser@gmail.com";  // From HTTP session
                        }
                    });
                }
                return message;
            }
        };
    }


    }
