package co.common.entities;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "audit_auth_fallos")
@ApiModel("Model Audit_Auth_fallos")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AuditAuthFallos implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de Auditoría de Logín fallidos", required = true)
	private Integer id;
	
	@Column(name = "id_usuario")
	@NotNull
	@ApiModelProperty(value = "número de identificación de Usuario", required = true)
	private String idUsuario;
	
	@Column(name = "login_usuario")
	@ApiModelProperty(value = "Username de Usuario que inicia sesión")
	@NotNull
	private String login;
	
	@Column(name = "ip_origen")
	@ApiModelProperty(value = "IP donde se inicia sesión")
	@NotNull
	private String ipOrigen;
	
	@Column(name = "fecha_intento")
	@ApiModelProperty(value = "Fecha y hora de acceso errado a la aplicación")
	@NotNull
	private Date fechaIntento;
	
	@Column(name = "descripcion")
	@ApiModelProperty(value = "Descripción de Inicio de sesión")
	private String descripcion;

}
