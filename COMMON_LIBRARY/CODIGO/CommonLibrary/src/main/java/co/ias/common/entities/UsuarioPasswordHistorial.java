package co.ias.common.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuario_password_historial")
@ApiModel("Model Usuario Password Historial")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class UsuarioPasswordHistorial implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de Usuario historico de passwords", required = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cod_usuario", referencedColumnName = "id")
	@JsonIgnore
	private Usuario usuario;

	@Column(name = "password")
	@NotNull
	@ApiModelProperty(value = "Password", required = true)
	private String password;

	@Column(name = "fecha")
	@NotNull
	@ApiModelProperty(value = "fecha de creación de password", required = true)
	private Date fecha;

	

}
