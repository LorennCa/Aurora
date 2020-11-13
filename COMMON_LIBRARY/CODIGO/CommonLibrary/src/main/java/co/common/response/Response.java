package co.common.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Api(tags = "Respuesta genérica compartida en todo el API")
public class Response {

	@ApiModelProperty(value="Código de la respuesta del servicio, típicamente un código HTTP status", required=false)
	private Integer code;
	@ApiModelProperty(value="Mensaje asociado a la respuesta de un servicio", required=false)
	private String message;
	@ApiModelProperty(value="Token de acceso único y con tiempo de expiración para usar servicios de este API",
			  required=false)
	private String accessToken;
	@ApiModelProperty(value="Carga o payload adicional que pueda tener esta respuesta", required=false)
	private String resource;
}