package co.ias.perseo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class ServiceGroupDTO extends SRDto {
	private static final long serialVersionUID = -5295522936216781055L;

	//@ApiModelProperty(value = "Unique ID for a service group", required = false)
	private Integer service_group_id;
	
	@ApiModelProperty(value = "Application name", required = true)
	private String service_group_name;
	
	@ApiModelProperty(value = "The type of this service group", required = true)
	private String service_group_type;
	
	@ApiModelProperty(value = "Business area this service group supports through its services", required = true)
	private String supported_business_area;
	
	@ApiModelProperty(value = "Description of the group", required = false)
	private String service_group_description;
}