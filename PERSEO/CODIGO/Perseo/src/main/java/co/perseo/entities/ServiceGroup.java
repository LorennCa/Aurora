package co.perseo.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sr_service_groups")
public class ServiceGroup implements SREntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer service_group_id;
	
	@Column(name = "service_group_name")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_group_name;
	
	@Column(name = "service_group_type")
	@ApiModelProperty(value = "")
	@NotNull
	private String service_group_type;
	
	@Column(name = "supported_business_area")
	@ApiModelProperty(value = "")
	private String supported_business_area;
	
	@Column(name = "service_group_description")
	@ApiModelProperty(value = "")
	private String service_group_description;
	
	@OneToMany(mappedBy = "service_id", cascade = CascadeType.ALL, orphanRemoval = true)
	@ApiModelProperty(value = "Lista de servicios")
	@OrderBy("service_id DESC")
	private List<Service> services;
	
	public ServiceGroup() {
	}
	
	public ServiceGroup(String service_group_name, String service_group_type, String supported_business_area,
			String service_group_description) {
		super();
		this.service_group_name = service_group_name;
		this.service_group_type = service_group_type;
		this.supported_business_area = supported_business_area;
		this.service_group_description = service_group_description;
	}
	@Override
	public String toString() {
		return "ServiceGroup [service_group_id=" + service_group_id + ", service_group_name=" + service_group_name
				+ ", service_group_type=" + service_group_type + ", supported_business_area=" + supported_business_area
				+ ", service_group_description=" + service_group_description + "]";
	}
	public Integer getService_group_id() {
		return service_group_id;
	}
	public void setService_group_id(Integer service_group_id) {
		this.service_group_id = service_group_id;
	}
	public String getService_group_name() {
		return service_group_name;
	}
	public void setService_group_name(String service_group_name) {
		this.service_group_name = service_group_name;
	}
	public String getService_group_type() {
		return service_group_type;
	}
	public void setService_group_type(String service_group_type) {
		this.service_group_type = service_group_type;
	}
	public String getSupported_business_area() {
		return supported_business_area;
	}
	public void setSupported_business_area(String supported_business_area) {
		this.supported_business_area = supported_business_area;
	}
	public String getService_group_description() {
		return service_group_description;
	}
	public void setService_group_description(String service_group_description) {
		this.service_group_description = service_group_description;
	}
}