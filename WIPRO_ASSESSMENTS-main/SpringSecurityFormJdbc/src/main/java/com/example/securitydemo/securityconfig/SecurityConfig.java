package com.example.securitydemo.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securitydemo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private final CustomUserDetailsService userDetailsService;

	// which of the below urls are secured / open and login method
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        .csrf(csrf ->csrf.disable()) //  to disable for simple rest demo ; enable for forms
	            .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/public/**").permitAll() // Allow public access to /public/**
	                .requestMatchers("/register").permitAll() 
	                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Require ADMIN role for /admin/**
	                .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN") // admin and user can access
	                .anyRequest().authenticated() // All other requests require authentication
	            )
	            //Request → Security Filter Chain → Authentication Manager → AuthenticationProvider → UserDetailsService.loadUserByUsername()
	            .userDetailsService(userDetailsService)
	            
	            //Spring creates this automatically:

				/*DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
				provider.setUserDetailsService(customUserDetailsService);
				provider.setPasswordEncoder(passwordEncoder);*/
	            .httpBasic(Customizer.withDefaults())
	            .formLogin(Customizer.withDefaults()); // by default login and logout urls are permitted for all 
	           
	        return http.build();
	    }
	 
	 @Bean
	    public PasswordEncoder passwordEncoder()
	    {
	    	return new BCryptPasswordEncoder();
	    }
	 
}
