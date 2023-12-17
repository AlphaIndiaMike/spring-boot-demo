package com.stag22.customer;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO>{
	@Override
	public CustomerDTO apply(Customer customer) {
		return new CustomerDTO(
				customer.getId(),
				customer.getName(),
				customer.getEmail(),
				customer.getGender(),
				customer.getAge(),
				customer.getAuthorities()
							.stream()
							.map(r -> r.getAuthority())
							.collect(Collectors.toList()),
				customer.getUsername()
		);
	}
}
