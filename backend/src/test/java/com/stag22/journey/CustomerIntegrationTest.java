package com.stag22.journey;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders;
import com.github.javafaker.Faker;
import com.stag22.customer.Customer;
import com.stag22.customer.CustomerDTO;
import com.stag22.customer.CustomerRegistrationRequest;
import com.stag22.customer.CustomerUpdateRequest;
import com.stag22.customer.Gender;

import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
	
	@Autowired
	private WebTestClient postmann;
	
	@Test
	void canRegisterACustomer() {
		//create registration request
		Faker FAKER = new Faker();
		String name = FAKER.name().fullName();
		String email = FAKER.name().firstName() + UUID.randomUUID() + "@gmail.com";
		int age = FAKER.random().nextInt(18, 90);
		Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
		
 		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
 				name, email, age, gender, "password");
		
		//send a post request
 		String jwtToken = postmann.post()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(request), CustomerRegistrationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.returnResult(Void.class)
 			.getResponseHeaders()
 			.get(HttpHeaders.AUTHORIZATION)
 			.get(0);
 		
		//get all customers
 		List<CustomerDTO> allCustomers = postmann.get()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
 			.returnResult()
 			.getResponseBody();
 		
		//make sure that customer is present
 		//Customer expected = new Customer(name, email, age, gender, "password");
		
		//get customer by id
 		var id = allCustomers.stream()
 				.filter(c -> c.email().equals(email))
 				.map(CustomerDTO::id)
 				.findFirst()
 				.orElseThrow();
 		
 		CustomerDTO expectedCustomer=new CustomerDTO(id, name, email, gender,age, List.of("ROLE_USER"), email);
			
 		assertThat(allCustomers).contains(expectedCustomer);
 		
 		postmann.get()
			.uri("/api/v1/customers" + "/{id}", id)
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
			.consumeWith(response ->
	        	assertThat(response.getResponseBody())
	        		.usingRecursiveComparison()
	        		.isEqualTo(expectedCustomer));
	}
	
	@Test
	void canDeleteCustomer() {
		//create registration request
		Faker FAKER = new Faker();
		String name = FAKER.name().fullName();
		String email = FAKER.name().firstName() + UUID.randomUUID() + "@gmail.com";
		int age = FAKER.random().nextInt(18, 90);
		Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
 		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
 				name, email, age, gender, "password");
		
		//send a post request
 		postmann.post()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(request), CustomerRegistrationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
		//get all customers
 		List<Customer> allCustomers = postmann.get()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.expectBodyList(new ParameterizedTypeReference<Customer>() {})
 			.returnResult()
 			.getResponseBody();
		
		//get customer by id
 		var id = allCustomers.stream()
 				.filter(c -> c.getEmail().equals(email))
 				.map(Customer::getId)
 				.findFirst()
 				.orElseThrow();
 		
 		//delete customer
 		postmann.delete()
 			.uri("/api/v1/customers" + "/{id}", id)
 			.accept(MediaType.APPLICATION_JSON)
 			.exchange()
 			.expectStatus()
 			.isOk();
 			
 		
 		postmann.get()
			.uri("/api/v1/customers" + "/{id}", id)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus()
			.isNotFound();
			
	}
	
	@Test
	void canUpdateCustomer() {
		//create registration request
		Faker FAKER = new Faker();
		String name = FAKER.name().fullName();
		String email = FAKER.name().firstName() + UUID.randomUUID() + "@gmail.com";
		int age = FAKER.random().nextInt(18, 90);
		Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
 		CustomerRegistrationRequest request = new CustomerRegistrationRequest(
 				name, email, age, gender, "password");
		
		//send a post request
 		postmann.post()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(request), CustomerRegistrationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
		//get all customers
 		List<Customer> allCustomers = postmann.get()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.expectBodyList(new ParameterizedTypeReference<Customer>() {})
 			.returnResult()
 			.getResponseBody();
		
		//get customer by id
 		var id = allCustomers.stream()
 				.filter(c -> c.getEmail().equals(email))
 				.map(Customer::getId)
 				.findFirst()
 				.orElseThrow();
 		
 		//update customer
 		String newName = "George";
 		
 		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
 				newName, null, null, null, null
 				);
 				
 		
 		postmann.put()
 			.uri("/api/v1/customers" + "/{id}", id)
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(updateRequest), CustomerUpdateRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
 		// test the update
 		Customer updatedCustomer = postmann.get()
			.uri("/api/v1/customers" + "/{id}", id)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(Customer.class)
			.returnResult()
			.getResponseBody();
			
 		Customer customer = new Customer(id, newName, email, age, gender, "password");
 		
 		assertThat(updatedCustomer)
 			.usingRecursiveComparison()
 			.isEqualTo(customer);
	}
}
