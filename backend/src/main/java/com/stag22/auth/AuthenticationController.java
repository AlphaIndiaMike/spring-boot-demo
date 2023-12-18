package com.stag22.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
	
	private final AuthenticationService authService;
	
	public AuthenticationController(AuthenticationService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
		AuthenticationResponse response = authService.login(request);
		return ResponseEntity
				.ok()
				.header(HttpHeaders.AUTHORIZATION, response.token())
				.body(response);
	}
}
