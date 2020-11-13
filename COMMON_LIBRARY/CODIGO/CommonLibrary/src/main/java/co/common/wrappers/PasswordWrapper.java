package co.common.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
public class PasswordWrapper {

	private Integer id;
	private String newPass;
	private String oldPass;
	
}
