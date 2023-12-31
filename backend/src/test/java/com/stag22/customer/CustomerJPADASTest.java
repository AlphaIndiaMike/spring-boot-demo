package com.stag22.customer;

import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CustomerJPADASTest {
	
	private CustomerJPADAS underTest;
	private AutoCloseable autoCloseable;
	
	@Mock
	private CustomerRepository customerRepository;

	@BeforeEach
	void setUp() throws Exception {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJPADAS(customerRepository);
		
	}
	
	@AfterEach
	void tearDown() throws Exception{
		autoCloseable.close();
	}

	@Test
	void testSelectAllCustomers() {
		//Given
		Page<Customer> page = mock(Page.class);
		List<Customer> customers = List.of(new Customer());
		Mockito.when(page.getContent()).thenReturn(customers);
		Mockito.when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
		//When
		List<Customer> expected = underTest.selectAllCustomers();
		//Then
		assertThat(expected).isEqualTo(customers);
		ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
		Mockito.verify(customerRepository).findAll(pageArgumentCaptor.capture());
		assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(1000));
	}

	@Test
	void testSelectCustomerById() {
		//Given
		int id = 1;
		
		//WHen
		underTest.selectCustomerById(id);
		
		//Then
		Mockito.verify(customerRepository).findById(id);
	}

	@Test
	void testInsertCustomer() {
		Customer customer = new Customer(
				1L, "Ali", "Ali@gmail.com", 18, Gender.MALE, "password");
		underTest.insertCustomer(customer);
		Mockito.verify(customerRepository).save(customer);
	}

	@Test
	void testExistsPersonWithEmail() {
		//given
		String email = "foo@gmail.com";
		
		//Given
		underTest.existsPersonWithEmail(email);
		
		//Then
		Mockito.verify(customerRepository).existsCustomerByEmail(email);
	}

	@Test
	void testDeleteCustomerById() {
		//Given
		int id = 1;
		//When
		underTest.deleteCustomerById(id);
		//Then
		Mockito.verify(customerRepository).deleteById(id);
	}

	@Test
	void testExistsPersonWithId() {
		//given
		Integer id = 10;
		
		//Given
		underTest.existsPersonWithId(id);
		
		//Then
		Mockito.verify(customerRepository).existsCustomerById(id);
	}

	@Test
	void testUpdateCustomer() {
		Customer customer = new Customer(
				1L, "Ali", "Ali@gmail.com", 18, Gender.MALE, "password");
		underTest.updateCustomer(customer);
		Mockito.verify(customerRepository).save(customer);
	}

}
