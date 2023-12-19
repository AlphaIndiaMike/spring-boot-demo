package com.stag22.customer;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

	@Test
	void testMapRow() throws SQLException{
		//given
		CustomerRowMapper cRM = new CustomerRowMapper();
		
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getLong("id")).thenReturn(1L);
		when(resultSet.getInt("age")).thenReturn(19);
		when(resultSet.getString("name")).thenReturn("Jamila");
		when(resultSet.getString("email")).thenReturn("Jamila@gmail.com");
		when(resultSet.getString("gender")).thenReturn("FEMALE"); 
		when(resultSet.getString("password")).thenReturn("password");
		
		//WHen
		Customer actual = cRM.mapRow(resultSet,1);
		
		//Then 
		Customer expected = new Customer(
				1L,"Jamila","Jamila@gmail.com",19,Gender.FEMALE, "password"
				);
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

}
