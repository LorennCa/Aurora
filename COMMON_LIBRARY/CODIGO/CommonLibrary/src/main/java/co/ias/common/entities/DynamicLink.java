package co.ias.common.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Table(name ="dynamic_links")
@Entity
@ApiModel("model dynamic links")
public class DynamicLink implements Serializable{

	private static final long serialVersionUID = 208134597707060994L;	
	@Id
	@Column(name = "link_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer link_id;	
	@Column(name = "confirmation_hash", nullable = true)
	private String confirmation_hash;	
	@Column(name = "user_login", nullable = false)
	private String user_login;
	@Column(name = "resource_url")
	private String resource_url;
	@Column(name = "expire_time")
	private Date expire_time;
	@Column(name = "subject_description")
	private String subject_description;
	@Column(name = "used")
	private boolean used;
	public DynamicLink(String userLogin, Date expireTime, boolean used) {
		setUser_login(userLogin);
		setUsed(used);
		setExpire_time(expireTime);
	}
	public DynamicLink(String userLogin) {
		setUser_login(userLogin);
	}
}