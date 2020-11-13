package co.heracles.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;;

@Entity
@Table(name = "configuracion_aurora")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Api(tags = "Modelo de configuración de seguridad del Aurora")
public class ConfiguracionHeracles  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value="Llave única de la entidad relacional, irrelevante para la aplicación.", required=true)
	private Integer id;
	
	@Column(name = "dias_expiracion_clave")
	@ApiModelProperty(value="Número de días para la expiración de la clave.", required=true)
	private Integer diasExpiracionClave;
	
	@Column(name = "validar_ip")
	@ApiModelProperty(value="Indica si se debe o no validar la IP del usuario que accede.", required=true)
	private boolean validarIP;
	
	@Column(name = "tiempo_expiracion_clave_temporal")
	@ApiModelProperty(value="Tiempo de expiración de la clave temporal en un reestablecimiento de contraseña.",
					  required=true)
	private Integer tiempo_expiracion_clave_temporal;
	
	@Column(name = "cantidad_intentos_permitidos")
	@ApiModelProperty(value="Número de intentos permitidos de login.", required=true)
	private Integer cantidadIntentosPermitidos;
	
	@Column(name = "conexion_activa_gluu")
	@ApiModelProperty(value="Obsoleto.", required=true)
	private boolean conexionActivaGluu;
	
	@Column(name = "intentos_reutilizar_password")
	@ApiModelProperty(value="Número de contraseñas anteriores que no se pueden reutilizar al cambiar la contraseña en "
							 , required=true)
	private Integer intentosReutilizarPassword;
	
	@Column(name = "consulta_deceval")
	private boolean consultaDeceval;
}