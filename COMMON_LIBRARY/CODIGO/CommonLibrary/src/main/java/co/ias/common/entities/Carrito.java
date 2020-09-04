package co.ias.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carrito")
@ApiModel("Model Inventario")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Carrito implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id_compra")
	private String id;
	
	@Column(name = "usuario")
	private String usuario;
	
	@Column(name = "referencia")
	private String referencia;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "recurso")
	private String recurso;
	
	@Column(name = "precio")
	private double precio;
	
}
