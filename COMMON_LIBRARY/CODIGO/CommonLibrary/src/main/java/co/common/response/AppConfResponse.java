package co.common.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Api(tags = "Respuesta específica del servicio de configuración")
public class AppConfResponse {
	@ApiModelProperty(value="Parámetro que indica si el captcha de Google está activado al momento de hacer login ",
			  		  required=true)
	private boolean captchaActivado;
	@ApiModelProperty(value="Ruta donde se encuentra el archivo de imagen con el logo principal en la página "
							 + "de login", required=true)
	private String logoPrincipal;
	@ApiModelProperty(value="Tiempo máximo de una sesión dentro", required=true)
	private Long tiempoSessionActiva;
	@ApiModelProperty(value="Número máximo de intentos de login permitidos", required=true)
	private int intentosPermitidosLogin;
	private boolean consultaDeceval;
	
}