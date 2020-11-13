package co.odin.config;

import org.apache.catalina.connector.Connector;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = { "co.odin.repositories" })
@SpringBootApplication(scanBasePackages = { "co.odin" })
@ComponentScan(basePackages = { "co.odin" })
@ComponentScan(basePackages = { "co.common.mailUtils" })
@EntityScan(basePackages = { "co.common.entities" })
public class OdinApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OdinApplication.class, args);		
	}
	
	@Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
	
	@SuppressWarnings("deprecation")
	@Bean
    public EmbeddedServletContainerFactory servletContainer() {
 
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
 
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setProtocol("AJP/1.3");
        ajpConnector.setPort(9090);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
        tomcat.addAdditionalTomcatConnectors(ajpConnector);
 
        return tomcat;
    }
	
}