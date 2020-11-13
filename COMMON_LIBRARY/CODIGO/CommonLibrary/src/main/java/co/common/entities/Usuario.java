package co.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "usuario")
@ApiModel("Model Usuario")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de Usuario", required = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cod_tipo_documento")
	@NotNull
	@ApiModelProperty(value = "tipo de documento de usuario", required = true)
	private TipoDocumento tipoDocumento;

	@Column(name = "numero_identificacion")
	@NotNull
	@ApiModelProperty(value = "número de identificación de Usuario", required = true)
	private String numeroIdentificacion;

	@ApiModelProperty(value = "Dígito de verificación del usuario si se trata de tipo de identificación NIT", required = false)
	@Column(name = "digito_verificacion")
	private Integer dv;

	@Size(max = 100)
	@Column(name = "idLDAP")
	@NotNull
	@ApiModelProperty(value = "Id generado por servidor LDAP", required = true)
	private String idLDAP;

	@Column(name = "nombres")
	@NotNull
	@ApiModelProperty(value = "nombres completos de Usuario", required = true)
	private String nombres;

	@Column(name = "apellidos")
	@NotNull
	@ApiModelProperty(value = "nombres completos de Usuario", required = true)
	private String apellidos;

	@Column(name = "direccion")
	@ApiModelProperty(value = "Dirección de un Usuario")
	private String direccion;

	@Column(name = "telefono")
	@ApiModelProperty(value = "Teléfono del Usuario")
	private String telefono;

	@Column(name = "email")
	@NotNull
	@ApiModelProperty(value = "Correo electrónico de un Usuario")
	private String email;

	@Column(name = "login")
	@ApiModelProperty(value = "Username de un Usuario para inicio de Sesión")
	@NotNull
	private String login;

	@Column(name = "password")
	@ApiModelProperty(value = "Contraseña de un Usuario para inicio de Sesión")
	@NotNull
	private String password;

	@ManyToOne
	@JoinColumn(name = "cod_perfil")
	@ApiModelProperty(value = "Perfil que tiene asignado el usuario")
	@NotNull
	private Perfil perfil;

	@Column(name = "estado")
	@ApiModelProperty(value = "El estado de un usuario puede ser: Bloqueado(B), Activo(A), Inactivo(I)")
	@NotNull
	private String estado;

	@Column(name = "ultimo_login")
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty(value = "Fecha y hora de último acceso a la aplicación")
	private Date ultimoLogin;

	@Column(name = "intentos_fallidos")
	@ApiModelProperty(value = "")
	private Integer intentosFallidos;

	@Column(name = "ultimo_cambio_clave")
	@ApiModelProperty(value = "Guarda el último cambio de clave del usuario")
	private Date ultimoCambioClave;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@ApiModelProperty(value = "Es una lista de direcciones IP permitidas")
	@NotNull
	private List<IpPermitida> listIpPermitidas;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "cod_usuario_rol_entidad")
	@ApiModelProperty(value = "Relación de Usuario y Rol que desempeña una Entidad a un Usuario")
	@NotNull
	private UsuarioRolEntidad usuarioRolEntidad;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	@ApiModelProperty(value = "Es una lista de histórico de contraseñas")
	@NotNull
	@OrderBy("fecha DESC")
	private List<UsuarioPasswordHistorial> historialPassword;

	@Column(name = "user_audit")
	private String userAudit;

	@Column(name = "ip_audit")
	private String ipAudit;

	@Column(name = "cambio_password")
	private boolean cambioClave;


}
