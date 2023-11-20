package com.stag22;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.javafaker.Faker;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@Testcontainers
public abstract class AbstractTestcontainersUnitTest {
	
	static {
        // Set Flyway logging to DEBUG level programmatically (assuming Logback is used)
        Logger flywayLogger = (Logger) LoggerFactory.getLogger("org.flywaydb");
        flywayLogger.setLevel(Level.DEBUG);
    }

    @BeforeAll
    static void beforeAll() {
        // Print out the configuration details
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();
        System.out.println("Configuring Flyway with:");
        System.out.println("JDBC URL: " + jdbcUrl);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .load();
        
        System.out.println("Starting migration...");
        flyway.migrate();
        System.out.println("Migration finished.");
    }
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Container
	protected static final PostgreSQLContainer<?> postgreSQLContainer = 
			new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("amigo-dao-unit-test")
			.withUsername("amigo")
			.withPassword("pass1233");
	
	@DynamicPropertySource
	private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}
	
	@SuppressWarnings("rawtypes")
	private static DataSource getDataSource() {
		DataSourceBuilder builder = DataSourceBuilder.create()
				.driverClassName(postgreSQLContainer.getDriverClassName())
				.url(postgreSQLContainer.getJdbcUrl())
				.username(postgreSQLContainer.getUsername())
				.password(postgreSQLContainer.getPassword());
		return builder.build();
	}
	
	protected static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}
	
	protected static final Faker FAKER = new Faker();

}
