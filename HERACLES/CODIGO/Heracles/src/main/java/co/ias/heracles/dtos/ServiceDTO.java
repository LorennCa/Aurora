package co.ias.heracles.dtos;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Esta clase deberá actualizarse desde el proyecto Perseo si llega a cambiar, esto es, eliminar o 
 * modificar atributos.
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@ApiModel("Service DTO")
public class ServiceDTO {
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
}