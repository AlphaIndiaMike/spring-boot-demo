package com.stag22.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDAS implements CustomerDao{
	
	private final JdbcTemplate JdbcTemplate;
	private final CustomerRowMapper customerRowMapper;

	public CustomerJDBCDAS(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
		this.JdbcTemplate = jdbcTemplate;
		this.customerRowMapper = customerRowMapper;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		// TODO: pagination
		var sql = """
				SELECT id, name, email, age, gender, password
				FROM customer
				LIMIT 1000
				""";
		List<Customer> customers = JdbcTemplate.query(sql, customerRowMapper);
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer Id) {
		var sql = """
				SELECT id, name, email, age, gender, password
				FROM customer
				WHERE id = ?
				""";
		return JdbcTemplate.query(sql, customerRowMapper, Id)
				.stream()
				.findFirst();
	}

	@Override
	public void insertCustomer(Customer customer) {
		// TODO Auto-generated method stub
		var sql = """
				INSERT INTO customer (name, email, age, gender, password)
				VALUES (?, ?, ?, ?, ?)
				""";
		int result = JdbcTemplate.update(
				sql,
				customer.getName(), 
				customer.getEmail(),
				customer.getAge(),
				customer.getGender().name(),
				customer.getPassword());
		System.out.println("jdbcTemplate.update = " + result);
	}

	@Override
	public boolean existsPersonWithEmail(String email) {
		var sql = """
				SELECT count(id)
				FROM customer
				WHERE email = ?
				""";
		Integer count = JdbcTemplate.queryForObject(sql, Integer.class, email);
		return count != null && count > 0;
	}

	@Override
	public boolean existsPersonWithId(Integer Id) {
		var sql = """
				SELECT count(id)
				FROM customer
				WHERE id = ?
				""";
		Integer count = JdbcTemplate.queryForObject(sql, Integer.class, Id);
		return count != null && count > 0;
	}

	@Override
	public void deleteCustomerById(Integer Id) {
		var sql = """
				DELETE 
				FROM customer
				WHERE id = ?
				""";
		int result = JdbcTemplate.update(sql, Id);
		System.out.println("deleteCustomerById result = "+ result);
		
	}

	@Override
	public void updateCustomer(Customer update) {
		if (update.getName() != null) {
			String sql = "UPDATE customer SET name = ? WHERE id = ?";
			int result = JdbcTemplate.update(sql, update.getName(), update.getId());
			System.out.println("update customer result = "+ result);
		}
		if (update.getAge() != 0) {
			String sql = "UPDATE customer SET age = ? WHERE id = ?";
			int result = JdbcTemplate.update(sql, update.getAge(), update.getId());
			System.out.println("update customer result = "+ result);
		}
		if (update.getEmail() != null) {
			String sql = "UPDATE customer SET email = ? WHERE id = ?";
			int result = JdbcTemplate.update(sql, update.getEmail(), update.getId());
			System.out.println("update customer result = "+ result);
		}
		if (update.getPassword() != null) {
			String sql = "UPDATE customer SET password = ? WHERE id = ?";
			int result = JdbcTemplate.update(sql, update.getPassword(), update.getId());
			System.out.println("update customer result = "+ result);
		}
		if (update.getGender() != null) {
			String sql = "UPDATE customer SET gender = ? WHERE id = ?";
			int result = JdbcTemplate.update(sql, update.getGender().name(), update.getId());
			System.out.println("update customer result = "+ result);
		}
	}

	@Override
	public Optional<Customer> selectUserByEmal(String email) {
		var sql = """
				SELECT id, name, email, age, gender, password
				FROM customer
				WHERE email = ?
				""";
		return JdbcTemplate.query(sql, customerRowMapper, email)
				.stream()
				.findFirst();
	}

}
