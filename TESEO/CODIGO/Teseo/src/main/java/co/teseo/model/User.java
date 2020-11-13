package co.teseo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auth_app_user")
@Getter @Setter @ToString @NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer appUserId;
	
	@Column(name = "app_user_name")
	private String app_user_name;
	
	public User(String userName) {
		setApp_user_name(userName);
	}
}