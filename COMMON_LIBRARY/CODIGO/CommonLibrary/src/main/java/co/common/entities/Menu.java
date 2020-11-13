package co.common.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "menu")
@ApiModel("modelo menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	@ApiModelProperty(value = "Id único de Menu", required = true)
	private Integer id;

	@Column(name = "nombre_opcion")
	@ApiModelProperty(value = "Nombre/Descripción de la opción de Menpú")
	private String nombreOpcion;

	@Column(name = "etiqueta")
	@NotNull
	@ApiModelProperty(value = "Etiqueta o Label de la opción de menú", required = true)
	private String etiqueta;

	@Column(name = "orden")
	@ApiModelProperty(value = "Entero que representa el orden dentro de la lista de menú de opciones")
	private Integer orden;

	@Column(name = "uri_servicio")
	@ApiModelProperty(value = "Contiene la uri del recurso para ser llamado")
	private String uriServicio;

	@Column(name = "tipo_menu")
	@ApiModelProperty(value = "Determina si es general o específico de una operación, puede ser: GENERAL, OPERACION")
	private String tipoMenu;

	@ApiModelProperty(value = "Determina el nivel de profundidad del árbol, puede ser: ROOT, PARENT_NODE ó FINAL_NODE")
	@Column(name = "nivel")
	private Integer nivel;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "menu_perfil", joinColumns = @JoinColumn(name = "cod_menu", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "cod_perfil", referencedColumnName = "id"))
	@JsonIgnore
	private List<Perfil> perfiles;

	@OneToOne
	@JoinColumn(name = "id_padre")
	@JsonIgnore
	private Menu menuPadre;

}
