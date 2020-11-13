package co.perseo.config.appconfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class SRAuthorizatorConfiguration extends WebMvcConfigurerAdapter{
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}