package co.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class OAuthResponse extends Response {
	private String errorDescription;
	private int status;
	private long expiresIn;
	
}