package co.common.entities;

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
@Table(name = "ciudades")
@ApiModel("Model ciudades")
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor
public class Ciudad implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de ciudades", required = true)
	private Integer id_ciudades;
	
	@Column(name = "paises_codigo")
	private String paisCodigo;
	
	@Column(name = "ciudad")
	private String ciudad;

}

