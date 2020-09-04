package co.ias.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "usuario_rol_entidad")
@ApiModel("Model UsuarioRolEntidad")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UsuarioRolEntidad implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de UsuarioEntidadRol", required = true)
	private Integer id;

	@ApiModelProperty(value = "Entidad", reference = "Entidad Model", required = true)
	@ManyToOne
	@JoinColumn(name = "cod_entidad")
	private Entidad entidad;

	@Column(name = "tipo_rol")
	@ApiModelProperty(value = "", required = true)
	private String tipoRol;
	

	

	
	

}
