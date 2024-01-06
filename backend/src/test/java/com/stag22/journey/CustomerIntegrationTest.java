package com.stag22.journey;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.com.google.common.io.Files;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders;
import com.github.javafaker.Faker;
import com.stag22.customer.Customer;
import com.stag22.customer.CustomerDTO;
import com.stag22.customer.CustomerRegistrationRequest;
import com.stag22.customer.CustomerUpdateRequest;
import com.stag22.customer.Gender;
import com.stag22.storage.AvatarStorageUtil;

import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
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
 		
 		CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
 				name, "c2"+email, age, gender, "password");
 		
 		// send a post request to create customer 1
 		postmann.post()
 	 			.uri("/api/v1/customers")
 	 			.accept(MediaType.APPLICATION_JSON)
 	 			.contentType(MediaType.APPLICATION_JSON)
 	 			.body(Mono.just(request), CustomerRegistrationRequest.class)
 	 			.exchange()
 	 			.expectStatus()
 	 			.isOk();
		
		//send a post request to create customer 2
 		String jwtToken = postmann.post()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(request2), CustomerRegistrationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.returnResult(Void.class)
 			.getResponseHeaders()
 			.get(HttpHeaders.AUTHORIZATION)
 			.get(0);
 		
		//get all customers
 		List<Customer> allCustomers = postmann.get()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
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
 		
 		//customer 2 deletes customer 1
 		postmann.delete()
 			.uri("/api/v1/customers" + "/{id}", id)
 			.accept(MediaType.APPLICATION_JSON)
 			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
 			.exchange()
 			.expectStatus()
 			.isOk();
 			
 		// customer 2 gets customer 1 by id
 		postmann.get()
			.uri("/api/v1/customers" + "/{id}", id)
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
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
		
		//get customer by id
 		var id = allCustomers.stream()
 				.filter(c -> c.email().equals(email))
 				.map(CustomerDTO::id)
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
 			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(updateRequest), CustomerUpdateRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
 		// test the update
 		CustomerDTO updatedCustomer = postmann.get()
			.uri("/api/v1/customers" + "/{id}", id)
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(CustomerDTO.class)
			.returnResult()
			.getResponseBody();
			
 		CustomerDTO customer = new CustomerDTO(id, newName, email, gender, age, List.of("ROLE_USER") ,email);
 		
 		assertThat(updatedCustomer)
 			.usingRecursiveComparison()
 			.isEqualTo(customer);
	}
	
	@Test
	void canUploadandDownloadProfilePicture() throws IOException {
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
		
		//get customer by id
 		var id = allCustomers.stream()
 				.filter(c -> c.email().equals(email))
 				.map(CustomerDTO::id)
 				.findFirst()
 				.orElseThrow();
 		
 		//When
 		Resource image = new ClassPathResource("male.png");
 		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
 		bodyBuilder.part("file", image);
 		
 		//{customerId}/profile-image
 		//send a post request
 		postmann.post()
 			.uri("/api/v1/customers" +"/{customerId}/profile-image", id)
 			.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
 			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
 		// Then the profile image shall be downloaded
 		byte[] downloadedImage = postmann.get()
			.uri("/api/v1/customers" + "/{id}/profile-image", id)
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(byte[].class)
			.returnResult()
			.getResponseBody();
 		
 		byte[] actual = Files.toByteArray(image.getFile());
 		
 		assertThat(actual).isEqualTo(downloadedImage);
 		
 		// TearDown
 		AvatarStorageUtil.drop_avatars();
 		
	}
}
