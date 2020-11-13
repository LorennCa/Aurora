package co.perseo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class ApplicationDTO extends SRDto {
	private static final long serialVersionUID = 7808466678257543870L;

	@ApiModelProperty(value = "Application unique identification number", required = false)
	private Integer application_id;
	
	@ApiModelProperty(value = "Name of the application", required = true)
	private String application_name;
	
	@ApiModelProperty(value = "Area which owns this application", required = true)
	private String parent_area_name;
	
	@ApiModelProperty(value = "Description of the application itself", required = false)
	private String application_description;
}