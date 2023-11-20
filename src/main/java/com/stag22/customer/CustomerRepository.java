package com.stag22.customer;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	
	boolean existsCustomerByEmail(String email);
	boolean existsCustomerById(Integer id);

}
