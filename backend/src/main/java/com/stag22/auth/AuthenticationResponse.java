package com.stag22.auth;

import com.stag22.customer.CustomerDTO;

public record AuthenticationResponse(
		String token,
		CustomerDTO customerDTO
		) {

}
