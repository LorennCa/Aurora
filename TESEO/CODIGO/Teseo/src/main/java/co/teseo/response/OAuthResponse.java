package co.teseo.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class OAuthResponse {

	private Integer code;
	private String message;
	private String accessToken;
	private String expireTime;
	private String resource;
	public OAuthResponse(Integer code, String message, String accessToken,
			String resource) {
		super();
		setCode(code);
		setMessage(message);
		setAccessToken(accessToken);
		setResource(resource);
	}
}