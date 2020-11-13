package co.heracles.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = { "co.heracles" })
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = { "co.heracles.repositories" })
@EntityScan(basePackages = { "co.heracles.entities" })
@ComponentScan(basePackages = { "co.heracles" })
public class HeraclesApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HeraclesApplication.class, args);		
	}
}