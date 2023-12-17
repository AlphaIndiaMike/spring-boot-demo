package com.stag22.customer;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Customer implements UserDetails {
	
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
	
	@Column(
			nullable = false
		)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(
			nullable = false
	)
	private String password;
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/* This is very important, if non existing JPA will fuck up!*/
	public Customer() {
	}
	
    public Customer(Long id, 
    				String name, 
    				String email, 
    				int age, 
    				Gender gender, 
    				String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }
    
    public Customer(String name, 
    				String email, 
    				int age, 
    				Gender gender, 
    				String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
    }

    /*
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
    }*/

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

	@Override
	public int hashCode() {
		return Objects.hash(age, email, gender, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return age == other.age && Objects.equals(email, other.email) && gender == other.gender
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ", gender=" + gender
				+ "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}


	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
