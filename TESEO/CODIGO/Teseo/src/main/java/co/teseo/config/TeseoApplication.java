package co.teseo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = { "co.teseo.repository" })
@SpringBootApplication(scanBasePackages = { "co.teseo" })
@EntityScan(basePackages = { "co.teseo.model" })
@ComponentScan(basePackages = { "co.teseo" })
public class TeseoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeseoApplication.class, args);
	}
}