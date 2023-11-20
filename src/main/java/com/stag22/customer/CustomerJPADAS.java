package com.stag22.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository("jpa")
public class CustomerJPADAS implements CustomerDao{
	
	private final CustomerRepository customerRepository;
	
	public CustomerJPADAS(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer Id) {
		return customerRepository.findById(Id);
	}

	@Override
	public void insertCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		return customerRepository.existsCustomerByEmail(email);
	}

	@Override
	public void deleteCustomerById(Integer Id) {
		customerRepository.deleteById(Id);
		
	}

	@Override
	public boolean existsPersonWithId(Integer Id) {
		return customerRepository.existsCustomerById(Id);
	}

	@Override
	public void updateCustomer(Customer update) {
		customerRepository.save(update);
	}

}
