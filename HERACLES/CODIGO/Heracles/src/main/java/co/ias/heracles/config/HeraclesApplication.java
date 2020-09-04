package co.ias.heracles.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = { "co.ias.heracles" })
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = { "co.ias.heracles.repositories" })
@EntityScan(basePackages = { "co.ias.heracles.entities" })
@ComponentScan(basePackages = { "co.ias.heracles" })
public class HeraclesApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HeraclesApplication.class, args);		
	}
}