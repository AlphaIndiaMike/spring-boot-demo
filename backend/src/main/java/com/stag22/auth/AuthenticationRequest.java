package com.stag22.auth;

public record AuthenticationRequest(
		String username,
		String password) {

}
