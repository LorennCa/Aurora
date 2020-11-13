package co.odin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(value="backend")
@Getter @Setter @ToString
public class GWConfigurations {
	private String server;
	private String port;
	private String fileserviceport;
}
