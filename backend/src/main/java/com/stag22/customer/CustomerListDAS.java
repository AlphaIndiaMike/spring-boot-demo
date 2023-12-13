package com.stag22.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

// Data Access Service
@Repository("list")
public class CustomerListDAS implements CustomerDao{
	private static List<Customer> customers;
	
	static {
		customers = new ArrayList<>();
		
		Customer alex = new Customer(
				1L,
				"Alex",
				"alex@gmail.com",
				21,
				Gender.MALE
		);
		customers.add(alex);
		
		// Now let's add more customers following the same pattern
	    Customer brian = new Customer(
	        2L,
	        "Brian",
	        "brian@example.com",
	        35,
	        Gender.MALE
	    );
	    customers.add(brian);

	    Customer charlotte = new Customer(
	        3L,
	        "Charlotte",
	        "charlotte@example.com",
	        28,
	        Gender.FEMALE
	    );
	    customers.add(charlotte);

	    Customer diana = new Customer(
	        4L,
	        "Diana",
	        "diana@example.com",
	        42,
	        Gender.FEMALE
	    );
	    customers.add(diana);
	}
	
	@Override
	public List<Customer> selectAllCustomers() {
		// TODO Auto-generated method stub
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer Id) {
		// TODO Auto-generated method stub
		return customers.stream()
					.filter(c -> c.getId().equals(Id))
					.findFirst();
					/*  */
		//return Optional.empty();
	}

	@Override
	public void insertCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customers.add(customer);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		return customers.stream()
				.anyMatch(c -> c.getEmail().equals(email));
		//return false;
	}

	@Override
	public void deleteCustomerById(Integer Id) {
		customers.stream()
		.filter(c -> c.getId().equals(Id))
		.findFirst()
		.ifPresent(customers::remove);
		
	}

	@Override
	public boolean existsPersonWithId(Integer Id) {
		return customers.stream()
				.anyMatch(c -> c.getId().equals(Id));
	}

	@Override
	public void updateCustomer(Customer update) {
		// TODO Auto-generated method stub
		customers.add(update);
	}

}
