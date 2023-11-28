package com.stag22;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;
import com.stag22.customer.Customer;
import com.stag22.customer.CustomerRepository;
import com.stag22.customer.Gender;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {

		/*
		 * Beans does: CustomerService customerService = new CustomerService(new
		 * CustomerDAS()); CustomerController customerController = new
		 * CustomerController(customerService);
		 */

		// SpringApplication.run(Main.class, args);
		@SuppressWarnings("unused")
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);


	}
	
	@Bean
	CommandLineRunner runner(CustomerRepository cRep) {
		var faker = new Faker();
		return args -> {
			Customer alex = new Customer(
					faker.name().fullName(),
					faker.internet().safeEmailAddress(),
					faker.number().numberBetween(18, 90),
					Gender.MALE
			);
		    cRep.save(alex);
		};
	}
	
	public static void printBeans(ConfigurableApplicationContext ctx) {
		
		String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
		for (String beanDefinitionName : beanDefinitionNames) {
			System.out.println(beanDefinitionName);
		}
	}

}
