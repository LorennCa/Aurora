package co.common.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfil")
@ApiModel("Model Perfil")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de perfil", required = true)
	private Integer id;

	@NotNull
	@Column(name = "nombre")
	@ApiModelProperty(value = "Nombre del perfil", required = true)
	private String nombre;

	@Column(name = "descripcion")
	@ApiModelProperty(value = "Descripción del Perfil", required = false)
	private String descripcion;

	@OneToOne
	@JoinColumn(name = "cod_perfil_superior")	
	@ApiModelProperty(value = "Perfil padre ó perfil superior", required = true)
	private Perfil perfilSuperior;
	

	@OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL)
	@JsonIgnore
	@ApiModelProperty(value = "lista de usuario que tienen este perfil", reference ="Usuario Model")
	private List<Usuario> usuarios;

	@ManyToMany(mappedBy = "perfiles", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	//@ManyToMany(mappedBy = "perfiles", cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
	private List<Menu> opcionesMenu;
	
	
	@JsonIgnore
	@Column(name = "user_audit")
	private String userAudit;
	
	@JsonIgnore
	@Column(name = "ip_audit")
	private String ipAudit;	
}