package co.common.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "diccionario_claves")
@ApiModel("Model Diccionario claves")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class DiccionarioClaves implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(value = "Id de Diccionario de claves", required = true)
	private Integer id;

	@Column(name = "cadena_claves")
	@ApiModelProperty(value = "Columna de cadena de las NO permitidas en el See_AdminService")
	@NotNull
	private String cadenaClaves;

}
