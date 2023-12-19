package com.stag22.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
	public List<Customer> selectAllCustomers();
	public Optional<Customer> selectCustomerById(Integer Id);
	public void insertCustomer(Customer customer);
	public boolean existsPersonWithEmail(String email);
	public boolean existsPersonWithId(Integer Id);
	public void deleteCustomerById(Integer Id);
	void updateCustomer(Customer update);
	Optional<Customer> selectUserByEmal(String email);
}
