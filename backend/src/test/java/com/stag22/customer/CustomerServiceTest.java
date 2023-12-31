package com.stag22.customer;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stag22.exception.DuplicateResourceException;
import com.stag22.exception.ResourceNotFoundException;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
	
	@Mock
	private CustomerDao customerDao;
	@Mock
	PasswordEncoder passwordEncoder;
	private CustomerService underTest;
	private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

	@BeforeEach
	void setUp() throws Exception {
		underTest = new CustomerService (customerDao, passwordEncoder, customerDTOMapper);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void getAllCustomers() {
		
		//when
		underTest.getAllCustomers();
		
		//then
		Mockito.verify(customerDao).selectAllCustomers();
		
	}
	
	@Test
	void canGetCustomer() {
		//Given
		long id = 10L;
		Customer customer = new Customer(
				id,
				"Alex",
				"alex@gmail.com",
				23,
				Gender.MALE, "password");
		Mockito.when(customerDao.selectCustomerById((int)(long)id))
			.thenReturn(Optional.of(customer));
		
		CustomerDTO expected = customerDTOMapper.apply(customer);
		
		// WHen
		CustomerDTO actual = underTest.getCustomer((int)(long)id);
		
		//Then
		assertThat(actual).isEqualTo(expected);
	}
	
	
	@Test
	void willThrowWhenGetCustomerReturnsEmptyOptional() {
		//Given
		long id = 10L;
		Mockito.when(customerDao.selectCustomerById((int)(long)id))
			.thenReturn(Optional.empty());
		
		// WHen
		//Customer actual = underTest.getCustomer((int)(long)id);
		
		//Then
		assertThatThrownBy(()-> underTest.getCustomer((int)(long)id))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("customer with id %s not found".formatted(id));
	}
	
	@Test
	void addCustomer() {
		//Given
		String email = "alex@gmail.com";

		Mockito.when(customerDao.existsPersonWithEmail(email))
			.thenReturn(false);
		
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
			"Alex", email, 19, Gender.MALE, "password"
		);
		
		String passwordHash = "hash_test_str";
		Mockito.when(passwordEncoder.encode("password")).thenReturn(passwordHash);
		//When
		underTest.addCustomer(request);
		
		//Then
		ArgumentCaptor<Customer> cAC = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).insertCustomer(cAC.capture());
		
		Customer captureCus = cAC.getValue();
		assertThat(captureCus.getId()).isNull();
		assertThat(captureCus.getName()).isEqualTo(request.name());
		assertThat(captureCus.getEmail()).isEqualTo(request.email());
		assertThat(captureCus.getAge()).isEqualTo(request.age());
		assertThat(captureCus.getPassword()).isEqualTo(passwordHash);
	}
	
	@Test
	void willThrowWhenEmailExistsWhileAddingCustomer() {
		//Given
		String email = "alex@gmail.com";

		Mockito.when(customerDao.existsPersonWithEmail(email))
			.thenReturn(true);
		
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
				"Alex", email, 19, Gender.MALE, "password"
			);
		
		//When
		assertThatThrownBy(() -> underTest.addCustomer(request))
			.isInstanceOf(DuplicateResourceException.class)
			.hasMessage("Customer email already exists!");
		
		//Then
		Mockito.verify(customerDao, never()).insertCustomer(any());//test will never insert anything.
	}
	
	@Test
	void deleteCustomerById() {
		//Given
		long id = 10L;
		
		Mockito.when(customerDao.existsPersonWithId((int)(long)id)).thenReturn(true);
		
		//When
		underTest.deleteCustomerById((int)(long)id);
		
		//Then
		Mockito.verify(customerDao).deleteCustomerById((int)(long)id);
		
	}
	
	@Test
	void willThrowWhenDeleteCustomerByIdNotExists() {
		//Given
		long id = 10L;
		
		Mockito.when(customerDao.existsPersonWithId((int)(long)id)).thenReturn(false);
		
		//When
		assertThatThrownBy(() -> underTest.deleteCustomerById((int)(long)id))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("customer with id [10] not found");
		
		//Then
		Mockito.verify(customerDao, never()).deleteCustomerById((int)(long)id);
		
	}
	
	@Test
	void updateCustomer() {
		//Given
		Integer id = 10;
		Customer customer = new Customer(
				(long)id,
				"Alex",
				"alex@gmail.com",
				23,
				Gender.MALE, "password");
		String newEmail = "john@gmail.com";
		Mockito.when(customerDao.selectCustomerById((int)(long)id))
			.thenReturn(Optional.of(customer));
		Mockito.when(customerDao.existsPersonWithId((int)(long)id)).thenReturn(true);
		
		CustomerUpdateRequest request = new CustomerUpdateRequest(
				"John", newEmail, 19, Gender.MALE, "password"
			);
		
		Mockito.when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
		
		//When
		underTest.updateCustomer(id, request);
		
		//Then
		ArgumentCaptor<Customer> cAC = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(cAC.capture());
		Customer capturedCustomer = cAC.getValue();
		
		assertThat(capturedCustomer.getName()).isEqualTo(request.name());
		assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
		assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
;	}

}
