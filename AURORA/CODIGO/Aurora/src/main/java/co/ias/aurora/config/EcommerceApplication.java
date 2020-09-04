package co.ias.aurora.config;

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
@SpringBootApplication(scanBasePackages = { "co.ias.aurora" })
@ComponentScan(basePackages = { "co.ias.aurora" })
@ComponentScan(basePackages = { "co.ias.common.mailUtils" })
@EntityScan(basePackages = { "co.ias.common.entities" })
@EnableJpaRepositories(basePackages = { "co.ias.aurora.repositories" })

public class EcommerceApplication {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		SpringApplication.run(EcommerceApplication.class, args);
		
	}
}