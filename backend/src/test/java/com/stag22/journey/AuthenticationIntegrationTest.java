package com.stag22.journey;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders;
import com.github.javafaker.Faker;
import com.stag22.auth.AuthenticationRequest;
import com.stag22.auth.AuthenticationResponse;
import com.stag22.customer.CustomerDTO;
import com.stag22.customer.CustomerRegistrationRequest;
import com.stag22.customer.Gender;
import com.stag22.jwt.JWTUtil;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntegrationTest {
	@Autowired
	private WebTestClient webTestClient;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final Random RANDOM = new Random();
	private static final String AUTH_PATH = "/api/v1/auth";
	
	@Test
	void canLogin() {
		//create registration request
		Faker FAKER = new Faker();
		String name = FAKER.name().fullName();
		String email = FAKER.name().firstName() + UUID.randomUUID() + "@gmail.com";
		int age = FAKER.random().nextInt(18, 90);
		Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
		
 		CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
 				name, email, age, gender, "password");
		
 		AuthenticationRequest authReq= new AuthenticationRequest(
 				email,
 				"password"
 		);
 		
 		// perform a login
 		webTestClient.post()
 			.uri(AUTH_PATH+"/login")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(authReq), AuthenticationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isUnauthorized();
 		
		//send a post request - create a user
 		webTestClient.post()
 			.uri("/api/v1/customers")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk();
 		
 		// perform the login
 		EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
 			.uri(AUTH_PATH+"/login")
 			.accept(MediaType.APPLICATION_JSON)
 			.contentType(MediaType.APPLICATION_JSON)
 			.body(Mono.just(authReq), AuthenticationRequest.class)
 			.exchange()
 			.expectStatus()
 			.isOk()
 			.expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
 				
 			})
 			.returnResult();
 		
 		String jwtToken = result
 				.getResponseHeaders()
 				.get(HttpHeaders.AUTHORIZATION)
 				.get(0);
 		
 		AuthenticationResponse authenticationResponse = result.getResponseBody();
 		CustomerDTO customerDTO = authenticationResponse.customerDTO();
 		
 		assertThat(
 				jwtUtil.isTokenValid(
 							jwtToken,
 							authenticationResponse.customerDTO().username()
 				)
 		);
 		
 		assertThat(customerDTO.email()).isEqualTo(email);
 		assertThat(customerDTO.age()).isEqualTo(age);
 		assertThat(customerDTO.name()).isEqualTo(name);
 		assertThat(customerDTO.gender()).isEqualTo(gender);
 		assertThat(customerDTO.username()).isEqualTo(email);
 		assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
 		
 		
	}
}
