package co.common.wrappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class ConfirmPasswordWrapper {
	private Integer id;
	private String newPass;
	private String oldPass;
}