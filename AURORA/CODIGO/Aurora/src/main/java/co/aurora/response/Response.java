package co.aurora.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Response {
	private Integer code;
	private String message;
	private String accessToken;
	private String resource;
}