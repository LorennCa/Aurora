package co.perseo.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sr_applications")
public class Application implements SREntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer application_id;
	
	@Column(name = "application_name")
	@ApiModelProperty(value = "", required = true)
	@NotNull
	private String application_name;
	
	@Column(name = "parent_area_name")
	@ApiModelProperty(value = "")
	private String parent_area_name;
	
	@Column(name = "application_description")
	@ApiModelProperty(value = "")
	private String application_description;
	
	@OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
	@JsonBackReference
	private Set<Service> services;
	
	public Application() {
	}
	
	public Application(String name, String area, String desc)
	{
		setApplication_name(name);
		setParent_area_name(area);
		setApplication_description(desc);
	}
	
	public void setServices(Set<Service> services) {
		this.services = services;
	}	
	public Integer getApplication_id() {
		return application_id;
	}
	public void setApplication_id(Integer application_id) {
		this.application_id = application_id;
	}
	public String getApplication_name() {
		return application_name;
	}
	public void setApplication_name(String application_name) {
		this.application_name = application_name;
	}
	public String getParent_area_name() {
		return parent_area_name;
	}
	public void setParent_area_name(String parent_area_name) {
		this.parent_area_name = parent_area_name;
	}
	public String getApplication_description() {
		return application_description;
	}
	public void setApplication_description(String application_description) {
		this.application_description = application_description;
	}
	public Set<Service> getServices() {
		return services;
	}

	@Override
	public String toString() {
		return "Application [id=" + application_id + ", name=" + application_name
				+ ", parent_area_name=" + parent_area_name + "]";
	}
}