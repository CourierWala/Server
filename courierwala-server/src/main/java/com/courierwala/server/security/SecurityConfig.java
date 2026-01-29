package com.courierwala.server.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.courierwala.server.config.CorsConfig;
import com.courierwala.server.jwtutils.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;

	private final JwtFilter jwtFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		
		http.csrf(csrf -> csrf.disable())
		         .cors(cors -> {})
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/api/staff/applyforjob")
						.permitAll()
						 .requestMatchers("/api/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/customer/**").hasRole("CUSTOMER")
					    .requestMatchers("/api/staff/**").hasRole("DELIVERY_STAFF")
						.anyRequest().authenticated())
				        .exceptionHandling(ex -> ex
		                .accessDeniedHandler(accessDeniedHandler)        // 403
		                .authenticationEntryPoint(authenticationEntryPoint) // 401
		            )
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
	


//	@Bean
//	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//				.csrf(csrf -> csrf.disable())
//				.cors(cors -> {})
//				.authorizeHttpRequests(auth ->
//						auth.anyRequest().permitAll()
//				)
//				.sessionManagement(session ->
//						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				);
//
//		return http.build();
//	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
