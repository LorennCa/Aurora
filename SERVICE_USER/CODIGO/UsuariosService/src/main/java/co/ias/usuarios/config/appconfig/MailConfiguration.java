package co.ias.usuarios.config.appconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@ConfigurationProperties("mailserver")
@Getter @Setter @lombok.ToString @NoArgsConstructor
public class MailConfiguration {
	private String mail_smtp_auth;
	private String mail_smtp_starttls_enable;
	private String mail_smtp_host;
	private String mail_smtp_port;
	private String mail_pass;
	private String mail_from;
	private String mail_default_password;
}