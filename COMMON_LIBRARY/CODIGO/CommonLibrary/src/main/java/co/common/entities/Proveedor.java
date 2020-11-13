package co.common.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@ApiModel("Model Proveedor")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Proveedor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idProveedor;
	
	@Column(name = "nombre_proveedor")
	private String nomProveedor;
	
	@Column(name = "direccion")
	private String dirProveedor;
	
	@Column(name = "ciudad")
	private String ciuProveedor;
	
	@Column(name = "telefono")
	private String telProveedor;
	
	@Column(name = "contacto")
	private String nomContacto;
	
	@Column(name = "telefono_contacto")
	private String telContacto;
	
	@Column(name = "fecha_creacion")
	private Date fecCreacion;
}
