package com.stag22.customer;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stag22.jwt.JWTUtil;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
	//Init business logic
	private CustomerService customerService;
	
	private JWTUtil jwtUtil;
	
	public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
		super();
		this.customerService = customerService;
		this.jwtUtil = jwtUtil;
	}

	/*
	@RequestMapping(
			path= "api/v1/customer", 
			method = RequestMethod.GET
	)*/
	@GetMapping
	public List<CustomerDTO> getCustomers(){
		return customerService.getAllCustomers();
	}
	
	@GetMapping("{customerId}")
	public CustomerDTO getCustomers(@PathVariable("customerId") Integer customerId){
		return customerService.getCustomer(customerId);
	}
	
	@PostMapping
	public ResponseEntity<?> registerCustomer(
			@RequestBody CustomerRegistrationRequest request) {
		customerService.addCustomer(request);
		String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, jwtToken)
			.build();
	}
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(
			@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomerById(customerId);
	}
	
	@PutMapping("{customerId}")
	public void updateCustomer(
			@PathVariable("customerId") Integer customerId,
			@RequestBody CustomerUpdateRequest request) {
		customerService.updateCustomer(customerId, request);
	}
	
	@PostMapping(
			value = "{customerId}/profile-image",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
			)
	public void uploadCustomerAvatar(
			@PathVariable("customerId") Integer customerId,
			@RequestParam("file") MultipartFile file
	) {
		customerService.setCustomerAvatar(customerId, file);
	}
	
	@GetMapping(
			value = "{customerId}/profile-image"
			)
	public byte[] getCustomerAvatar(
			@PathVariable("customerId") Integer customerId
	) {
		return customerService.getCustomerAvatar(customerId);
	}
	
	@GetMapping("/greet")
	public GreetResponse greet(
			@RequestParam(value = "name", required = false) String name) {
		String greetMessage = name == null || name.isBlank() ? "Hello" : "Hello " + name;
		GreetResponse response =  new GreetResponse(
					greetMessage,
					List.of("Java","Golang","Javascript"),
					new Person("Alex", 32, 30_000)
				);
		return response;
	}
	
	record Person(String name, int age, double savings) {
		
	}
	
	record GreetResponse(
			String greet,
			List<String> favProgrammingLanguages, 
			Person person) {
		
	}
}
