package com.stag22.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.stag22.jwt.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
	
	private AuthenticationProvider authProvider;
	private JWTAuthenticationFilter jwtAuthFilter;
	private AuthenticationEntryPoint authEntryPoint;
	
	public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,
			JWTAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authProvider = authenticationProvider;
		this.jwtAuthFilter = jwtAuthenticationFilter;
		this.authEntryPoint = authenticationEntryPoint;
	}
	
	@SuppressWarnings({ "removal" })
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.POST, "/api/v1/customers", "/api/v1/auth/login")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(authProvider)
			.addFilterBefore(
				jwtAuthFilter,
				UsernamePasswordAuthenticationFilter.class
			)
			.exceptionHandling()
			.authenticationEntryPoint(authEntryPoint);
		
		return http.build();
	}
}
