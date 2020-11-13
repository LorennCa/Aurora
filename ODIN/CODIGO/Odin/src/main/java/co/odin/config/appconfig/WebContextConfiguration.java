package co.odin.config.appconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Getter @Setter
@Component
@ConfigurationProperties("server")
public class WebContextConfiguration {
	
	private Integer port;
	private String see_protocol;
	private String see_host;
	private String see_web_context;
}