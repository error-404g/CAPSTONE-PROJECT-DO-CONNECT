package com.example.securitydemo.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.securitydemo.entity.AppUser;
import com.example.securitydemo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		AppUser user = repo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found: " + username));
		
		System.out.println("The details of users are given :");
		return new User( 
						user.getUsername(),
						user.getPassword(),
						//Spring Security does NOT work with roles as plain Strings.
						//It expects them wrapped in an object called GrantedAuthority.
						//GrantedAuthority("ROLE_USER")GrantedAuthority("ROLE_ADMIN")
						//puts it into a List because Spring Security expects:Collection<? extends GrantedAuthority>
						List.of(new SimpleGrantedAuthority(user.getRole()))
					  /*Instead of manually doing it If user has multiple roles in DB like:"ROLE_USER,ROLE_ADMIN" spring does Authorities = ["ROLE_USER"]
					   * List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRole().split(","))
        .map(SimpleGrantedAuthority::new)
        .toList();*/	
						);
	}

}
