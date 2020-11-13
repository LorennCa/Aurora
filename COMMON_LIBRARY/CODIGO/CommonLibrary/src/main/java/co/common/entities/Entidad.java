package co.common.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "entidad")
@ApiModel("Model entidad")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Entidad implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id único de Entidad", required = true)
	private Integer id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "NIT")
	private String NIT;

	@Column(name = "emisor")
	private boolean emisor;

	@Column(name = "afiliado")
	private boolean afiliado;


	@Column(name = "super_admin")
	private boolean superAdmin;

	@OneToMany(mappedBy = "entidad", cascade = CascadeType.ALL)
	@JsonIgnore
	@ApiModelProperty(value = "lista de asociaciones usuarioRolEntidad", reference = "UsuarioRolEntidad Model")
	private List<UsuarioRolEntidad> usuarioRolEntidad;
	
	
	}