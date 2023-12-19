package com.stag22.customer;

public record CustomerRegistrationRequest(
		String name,
		String email,
		Integer age,
		Gender gender,
		String password
		){

}
