package com.stag22.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.stag22.customer.Customer;
import com.stag22.customer.CustomerDTO;
import com.stag22.customer.CustomerDTOMapper;
import com.stag22.jwt.JWTUtil;

@Service
public class AuthenticationService {
	
	private final AuthenticationManager authenticationManager;
	private final CustomerDTOMapper customerDTOMapper;
	private final JWTUtil jwtUtil;
	
	public AuthenticationService(
			AuthenticationManager authenticationManager, 
			CustomerDTOMapper customerDTOMapper, 
			JWTUtil jwtUtil
	) {
		super();
		this.authenticationManager = authenticationManager;
		this.customerDTOMapper = customerDTOMapper;
		this.jwtUtil = jwtUtil;
	}

	public AuthenticationResponse login(AuthenticationRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.username(),
						request.password()
				)
		);
		Customer principal = (Customer) authentication.getPrincipal();
		CustomerDTO customerDTO = customerDTOMapper.apply(principal);
		String token = jwtUtil.issueToken(customerDTO.username(),customerDTO.roles());
		
		return new AuthenticationResponse(token, customerDTO);
	}
}
