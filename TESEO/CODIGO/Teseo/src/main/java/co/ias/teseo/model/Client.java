package co.ias.teseo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "auth_app_client")
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer appClientId;
	
	@Column(name = "app_client_name")
	@NotNull
	private String appClientName;
	
	@Column(name = "app_inum")
	@NotNull
	private String appInum;

	public Integer getAppClientId() {
		return appClientId;
	}

	public void setAppClientId(Integer appClientId) {
		this.appClientId = appClientId;
	}

	public String getAppClientName() {
		return appClientName;
	}

	public void setAppClientName(String appClientName) {
		this.appClientName = appClientName;
	}

	public String getAppInum() {
		return appInum;
	}

	public void setAppInum(String appInum) {
		this.appInum = appInum;
	}
	
	@Override
	public String toString() {
		return "Client: [" + appClientId + "|" + appClientName + "|" + appInum + "]";
	}
}