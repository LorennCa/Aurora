package co.ias.common.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;;

@Entity
@Table(name = "configuracion_ias")
@ApiModel("model configuracion")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class ConfiguracionIAS  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "dias_expiracion_clave")
	private Integer diasExpiracionClave;
	
	@Column(name = "validar_ip")
	private boolean validarIP;
	
	@Column(name = "tiempo_expiracion_clave_temporal")
	private Integer tiempo_expiracion_clave_temporal;
	
	@Column(name = "cantidad_intentos_permitidos")
	private Integer cantidadIntentosPermitidos;
	
	@Column(name = "conexion_activa_gluu")
	private boolean conexionActivaGluu;
	
	@Column(name = "intentos_reutilizar_password")
	private Integer intentosReutilizarPassword;
	
	@Column(name = "consulta_deceval")
	private boolean consultaDeceval;
}