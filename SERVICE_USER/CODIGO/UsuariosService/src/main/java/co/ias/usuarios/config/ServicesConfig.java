package co.ias.usuarios.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix="services")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class ServicesConfig {
	private List<Service> services = new ArrayList<>();
}