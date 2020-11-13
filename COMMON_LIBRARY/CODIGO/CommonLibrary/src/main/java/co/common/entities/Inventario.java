package co.common.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario")
@ApiModel("Model Inventario")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Inventario implements Serializable{
private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private InventarioPK clave;
	
	@Column(name = "precio")
	private double precio;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@Column(name = "recurso_1")
	private String recursoUno;
	
	@Column(name = "recurso_2")
	private String recursoDos;
	
	@Column(name = "recurso_3")
	private String recursoTres;
	
	@Column(name = "recurso_4")
	private String recursoCuatro;
	
	@Column(name = "recurso_5")
	private String recursoCinco;
	
	@Column(name = "palabra_clave")
	private String palabra_clave;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "fecha_creacion")
	private Date fecha_creacion;
	
	@Column(name = "fecha_modificacion")
	private Date fecha_modificacion;
	
}

