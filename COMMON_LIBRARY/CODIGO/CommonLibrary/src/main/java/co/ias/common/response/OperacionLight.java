package co.ias.common.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class OperacionLight {
	
	private Integer id;
	private String nombre;
	private String rutaLogo;
	private Date fechaInicio;
	private Date fechaFin;
	

	

}
