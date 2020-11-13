package co.aquiles.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = { "co.aquiles" })
@ComponentScan(basePackages = { "co.aquiles" })
@ComponentScan(basePackages = { "co.common.mailUtils" })
@EntityScan(basePackages = { "co.common.entities" })
@EnableJpaRepositories(basePackages = { "co.aquiles.repositories" })

public class AquilesApplication {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		SpringApplication.run(AquilesApplication.class, args);
		
	}
}