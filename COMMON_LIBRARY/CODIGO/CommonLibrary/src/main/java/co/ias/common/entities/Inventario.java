package co.ias.common.entities;

import java.io.Serializable;

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
	
	@Column(name = "recurso")
	private String recurso;
	
	@Column(name = "palabra_clave")
	private String palabra_clave;
	
}

