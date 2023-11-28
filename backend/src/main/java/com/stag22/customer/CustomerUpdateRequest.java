package com.stag22.customer;

public record CustomerUpdateRequest(
		String name,
		String email,
		Integer age,
		Gender gender
		) {

}
