package co.ias.perseo.dtos;

import java.util.Date;

import co.ias.perseo.entities.Service;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@ApiModel("Service DTO")
public class ServiceDTO extends SRDto {
	private static final long serialVersionUID = 1389585612099088892L;
	private Integer service_id;
	@ApiModelProperty(value = "Group service ID", required = true)
	private Integer service_group_id;
	@ApiModelProperty(value = "Parent application ID", required = false)
	private Integer application_id;
	@ApiModelProperty(value = "Service name", required = true)
	private String service_name;
	@ApiModelProperty(value = "Owner of the service", required = true)
	private Integer service_owner_id;
	@ApiModelProperty(value = "Service protocol", required = false)
	private String service_protocol;
	@ApiModelProperty(value = "Host the service is deployed on", required = true)
	private String service_host_ip;
	@ApiModelProperty(value = "Port the service is listening on", required = true)
	private String service_port;
	@ApiModelProperty(value = "Service root http context", required = true)
	private String service_root_context;
	@ApiModelProperty(value = "If the service is active", required = true)
	private boolean service_active;
	@ApiModelProperty(value = "Service description", required = false)
	private String service_description;
	@ApiModelProperty(value = "Who wrote this service code", required = false)
	private String service_author;
	@ApiModelProperty(value = "IT area the service belongs to", required = true)
	private String service_it_area;
	@ApiModelProperty(value = "Date and hour the service was registered", required = true)
	private Date registration_date;
	
	public Service toEntity() {
		Service entity = new Service();
		entity.setRegistration_date(registration_date);
		entity.setService_active(service_active);
		entity.setService_name(service_name);
		entity.setService_it_area(service_it_area);
		entity.setService_author(service_author);
		entity.setService_description(service_description);		
		entity.setService_host_ip(service_host_ip);
		entity.setService_owner_id(service_owner_id);
		entity.setService_port(service_port);
		entity.setService_protocol(service_protocol);
		entity.setService_root_context(service_root_context);
		return entity;
	}
}