package com.stag22.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import com.stag22.AbstractTestcontainersUnitTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersUnitTest{
	
	@Autowired
	private CustomerRepository underTest;
	
	@Autowired
	private ApplicationContext applicationContext;

	@BeforeEach
	void setUp() throws Exception {
		//underTest.deleteAll();
		System.out.println(applicationContext.getBeanDefinitionCount());
	}

	@Test
	void testExistsCustomerById() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20,
				Gender.MALE);
		
		underTest.save(customer);

		Long id = underTest.findAll()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();
		
		// When
		var actual = underTest.existsCustomerById((int)(long)id);
		
		// Then
		assertThat(actual).isTrue();
	}
	
	@Test
	void testExistsCustomerByIdWhenIdNotPresent() {
		//Given
		int id = -1;
		
		// When
		var actual = underTest.existsCustomerById(id);
		
		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void testExistsCustomerByEmail() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		Customer customer = new Customer(
				FAKER.name().fullName(),
				email,
				20,
				Gender.MALE);
		
		underTest.save(customer);
		
		// When
		var actual = underTest.existsCustomerByEmail(email);
		
		// Then
		assertThat(actual).isTrue();
	}
	
	@Test
	void testExistsCustomerByEmailWhenEmailNotPresent() {
		//Given
		String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
		
		// When
		var actual = underTest.existsCustomerByEmail(email);
		
		// Then
		assertThat(actual).isFalse();
	}

}
