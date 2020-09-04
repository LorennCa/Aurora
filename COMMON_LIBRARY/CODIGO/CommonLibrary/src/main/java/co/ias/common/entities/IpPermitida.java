package co.ias.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ip_permitida")
@ApiModel("modelo ip permitida")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class IpPermitida implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "ip")
	private String ip;
	
	@ManyToOne
	@JoinColumn(name = "cod_usuario", referencedColumnName ="id")
	@JsonIgnore
	private Usuario usuario;
	
}
