package com.stag22.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	
	boolean existsCustomerByEmail(String email);
	boolean existsCustomerById(Integer id);
	Optional<Customer> findCustomerByEmail(String email);

}
