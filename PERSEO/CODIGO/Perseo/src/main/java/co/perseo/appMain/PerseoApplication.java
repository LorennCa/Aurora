package co.perseo.appMain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "co.perseo.repository" })
@SpringBootApplication(scanBasePackages = { "co.perseo" })
@EntityScan(basePackages = { "co.perseo.entities" })
@ComponentScan(basePackages = { "co.perseo" })
public class PerseoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerseoApplication.class, args);
	}
}