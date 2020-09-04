package co.ias.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InventarioPK implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "id")
	private String id_producto;
	
	@Column(name = "referencia")
	private String referencia;
	
	@Column(name = "color")
	private String color;

}
