package com.stag22.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.stag22.AbstractTestcontainersUnitTest;

class CustomerJDBCDASTest extends AbstractTestcontainersUnitTest{
	
	private static CustomerJDBCDAS underTest;
	private final static CustomerRowMapper customerRowMapper = new CustomerRowMapper();

	@BeforeEach
	void setUpBeforeEachClass(){
		underTest = new CustomerJDBCDAS(
				getJdbcTemplate(),
				customerRowMapper);
	}

	@Test
	void testCustomerJDBCDAS() {
	}

	@Test
	void testSelectAllCustomers() {
		Customer customer = new Customer(
				FAKER.name().fullName(),
				FAKER.internet().safeEmailAddress()+ "-" +UUID.randomUUID(),
				20
				);
		underTest.insertCustomer(customer);
		//When
		List<Customer> customers = underTest.selectAllCustomers();
		
		//Then
		assertThat(customers).isNotEmpty();
	}

	@Test
	void testSelectCustomerById() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);

		Long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		// When
		Optional<Customer> actual = underTest.selectCustomerById((int) (long) id);
		
		// Then
		assertThat(actual).isPresent().hasValueSatisfying( c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}
	
	@Test
	void willReturnEmptyWhenSelectCustomerById() {
		//Given
		var id = -1;
		
		// When
		var actual = underTest.selectCustomerById(id);
		
		//Then
		assertThat(actual).isEmpty();		
	}

	@Test
	void testInsertCustomer() {
	}

	@Test
	void testExistsPersonWithEmail() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		//when
		boolean actual = underTest.existsPersonWithEmail(email);
		
		//Then 
		assertThat(actual).isTrue();
	}
	
	@Test
	void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

		//when
		boolean actual = underTest.existsPersonWithEmail(email);
		
		//Then 
		assertThat(actual).isFalse();
	}

	@Test
	void testExistsCustomerWithId() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		//When
		var actual = underTest.existsPersonWithId((int)(long)id);
		
		//Then
		assertThat(actual).isTrue();
		
	}
	
	@Test
	void existsCusrtomerWithIdWillReturnFalseWhenIdNotPresent() {
		//Given
		var id = -1;
		
		// WHen
		var actual = underTest.existsPersonWithId(id);
		
		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void testDeleteCustomerById() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		//When
		underTest.deleteCustomerById((int)(long)id);
		
		//Then
		Optional<Customer> actual = underTest.selectCustomerById((int)(long)id);
		assertThat(actual).isNotPresent();
		
	}

	@Test
	void testUpdateCustomerName() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		var newName = "Foo";
		
		//When
		Customer update = new Customer();
		update.setId(id);
		update.setName(newName);
		
		underTest.updateCustomer(update);
		
		//Then
		Optional<Customer> actual = underTest.selectCustomerById((int)(long)id);
		assertThat(actual).isPresent().hasValueSatisfying( c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(newName);
			assertThat(c.getEmail()).isEqualTo(email);
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}
	
	@Test
	void testUpdateCustomerEmail() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		var newEmail = "test@google.com";
		
		//When
		Customer update = new Customer();
		update.setId(id);
		update.setEmail(newEmail);
		
		underTest.updateCustomer(update);
		
		//Then
		Optional<Customer> actual = underTest.selectCustomerById((int)(long)id);
		assertThat(actual).isPresent().hasValueSatisfying( c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(newEmail);
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}
	
	@Test
	void testUpdateCustomerAge() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		var newAge = 7;
		
		//When
		Customer update = new Customer();
		update.setId(id);
		update.setAge(newAge);
		
		underTest.updateCustomer(update);
		
		//Then
		Optional<Customer> actual = underTest.selectCustomerById((int)(long)id);
		assertThat(actual).isPresent().hasValueSatisfying( c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(email);
			assertThat(c.getAge()).isEqualTo(newAge);
		});
	}
	
	
	@Test
	void testWillNotUpdateWhenNothingToUpdate() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20);
		
		underTest.insertCustomer(customer);
		
		long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		//When
		Customer update = new Customer();
		update.setId(id);
		
		underTest.updateCustomer(update);
		
		//Then
		Optional<Customer> actual = underTest.selectCustomerById((int)(long)id);
		assertThat(actual).isPresent().hasValueSatisfying( c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(email);
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}
	

}
