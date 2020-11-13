package co.odin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(value="service")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Service {
	private String name;
	private String protocol;
    private String host;
    private int port;
    private String serviceContext;
    public String getServiceURL() {    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(protocol).append(host).append(":").append(port).append(serviceContext);
    	return sb.toString();
    }
}