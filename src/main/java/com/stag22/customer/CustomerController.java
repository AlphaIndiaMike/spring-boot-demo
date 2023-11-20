package com.stag22.customer;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
	//Init business logic
	private CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	/*
	@RequestMapping(
			path= "api/v1/customer", 
			method = RequestMethod.GET
	)*/
	@GetMapping
	public List<Customer> getCustomers(){
		return customerService.getAllCustomers();
	}
	
	@GetMapping("{customerId}")
	public Customer getCustomers(@PathVariable("customerId") Integer customerId){
		return customerService.getCustomer(customerId);
	}
	
	@PostMapping
	public void registerCustomer(
			@RequestBody CustomerRegistrationRequest request) {
		customerService.addCustomer(request);
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
