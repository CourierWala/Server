package com.courierwala.server.security;


import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.courierwala.server.entities.User;
import com.courierwala.server.exception.ResourceNotFoundException;
import com.courierwala.server.repository.UserRepository;

//import com.security.utils.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            
		 
		User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("user not exists, Invalid Credential !"));
		
		 
		List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole().toString()));

		 return new CustomUserDetails(
	                user.getId(),
	                user.getEmail(),
	                user.getPassword(),
	                user.getName(),
	                authorities
	        );
		
	}


}
