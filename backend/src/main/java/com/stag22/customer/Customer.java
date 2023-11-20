package com.stag22.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "customer",
	uniqueConstraints = {
			@UniqueConstraint(
					name = "customer_email_unique",
					columnNames = "email"
		)
	}
)
public class Customer {
	
	@Id
	@SequenceGenerator(
			name = "customer_id_seq",
			sequenceName = "customer_id_seq",
			allocationSize = 1
			)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "customer_id_seq"
			)
    private Long id;
	@Column(
			nullable = false	
		)
    private String name;
	@Column(
			nullable = false
		)
    private String email;
	@Column(
			nullable = false	
		)
    private int age;
	
	/* This is very important, if non existing JPA will fuck up!*/
	public Customer() {
	}

    public Customer(Long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
