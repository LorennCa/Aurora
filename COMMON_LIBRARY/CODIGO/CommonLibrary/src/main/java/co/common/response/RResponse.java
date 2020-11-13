package co.common.response;

import javax.mail.internet.MimeMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class RResponse extends Response {
	private String action_url;
	private String year;
	private String os;
	private String browser;
	private String resetRequestUniqueID;
	private String temporalPassword;
	private MimeMessage emailMessage;
}