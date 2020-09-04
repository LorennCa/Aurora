package co.ias.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.exolab.castor.types.DateTime;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuario_password_historial")
@ApiModel("model password historial")

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class PasswordHistorial implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;

@Column
private Integer usuario;

@Column 
private String password;

@Column 
private DateTime fecha;
}
