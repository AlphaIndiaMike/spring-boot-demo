package com.stag22.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stag22.exception.DuplicateResourceException;
import com.stag22.exception.RequestValidationException;
import com.stag22.exception.ResourceNotFoundException;

@Service
public class CustomerService {
	private final CustomerDao customerDao;
	private final PasswordEncoder passwordEncoder;
	private final CustomerDTOMapper customerDTOMapper;
	
	// here change the implementation from Qualifier
	public CustomerService(
			@Qualifier("jdbc") CustomerDao customerDao,
			PasswordEncoder passwordEncoder,
			CustomerDTOMapper customerDTOMapper) {
		this.customerDao = customerDao;
		this.passwordEncoder = passwordEncoder;
		this.customerDTOMapper = customerDTOMapper;
	}
	
	
	public List<CustomerDTO> getAllCustomers(){
		return customerDao.selectAllCustomers()
				.stream()
				.map(customerDTOMapper)
				.collect(Collectors.toList());
	}
	
	public CustomerDTO getCustomer(Integer id) {
		return customerDao.selectCustomerById(id)
				.map(customerDTOMapper)
				.orElseThrow(
					() -> new ResourceNotFoundException("customer with id %s not found!".formatted(id))
				);
	}
	
	public void addCustomer(CustomerRegistrationRequest customerRegReq) {
		//check if e-mail exist
		if (customerDao.existsPersonWithEmail(customerRegReq.email())) {
			throw new DuplicateResourceException("Customer email already exists!");
		}
		else
		{
			Customer customer = new Customer(
					customerRegReq.name(),
					customerRegReq.email(),
					customerRegReq.age(),
					customerRegReq.gender(), 
					passwordEncoder.encode(customerRegReq.password())
					);
					
			customerDao.insertCustomer(customer);
		}
	}


	public void deleteCustomerById(Integer customerId) {
		if (!customerDao.existsPersonWithId(customerId)) {
			throw new ResourceNotFoundException(
					"customer with id [%s] not found".formatted(customerId));
		}
		customerDao.deleteCustomerById(customerId);
		
	}
	
	public void updateCustomer(Integer customerId, 
							   CustomerUpdateRequest customerUpdReq) {
		if (!customerDao.existsPersonWithId(customerId)) {
			throw new ResourceNotFoundException(
					"customer with id [%s] not found".formatted(customerId));
		}
		//Customer customer = getCustomer(customerId);
		Customer customer = customerDao.selectCustomerById(customerId)
								.orElseThrow(
										() -> new ResourceNotFoundException(
												"customer with id %s not found!".formatted(customerId)
											)
							);
		
		boolean changes = false;
		
		if (customerUpdReq.name() != null && !customerUpdReq.name().equals(customer.getName())) {
			customer.setName(customerUpdReq.name());
			changes = true;
		}
		if (customerUpdReq.age() != null && !customerUpdReq.age().equals(customer.getAge())) {
			customer.setAge(customerUpdReq.age());
			changes = true;
		}
		if (customerUpdReq.gender() != null && !customerUpdReq.gender().equals(customer.getGender())) {
			customer.setGender(customerUpdReq.gender());
			changes = true;
		}
		if (customerUpdReq.email() != null && !customerUpdReq.email().equals(customer.getEmail())) {
			if (customerDao.existsPersonWithEmail(customerUpdReq.email())) {
				throw new DuplicateResourceException(
						"email already taken"
						);
			}
			
			customer.setEmail(customerUpdReq.email());
			changes = true;
		}
		
		if (!changes) {
			throw new RequestValidationException("no data changes found");
		}
				
		customerDao.updateCustomer(customer);
	}
	
}
