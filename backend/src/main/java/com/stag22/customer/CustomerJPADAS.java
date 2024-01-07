package com.stag22.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public class CustomerJPADAS implements CustomerDao{
	
	private final CustomerRepository customerRepository;
	
	public CustomerJPADAS(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		Page<Customer> page = customerRepository.findAll(Pageable.ofSize(1000));
		return page.getContent();
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

	@Override
	public Optional<Customer> selectUserByEmal(String email) {
		return customerRepository.findCustomerByEmail(email);
	}

}
