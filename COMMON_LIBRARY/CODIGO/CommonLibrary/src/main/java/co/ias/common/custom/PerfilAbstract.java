package co.ias.common.custom;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PerfilAbstract {

	private Integer id;
	private String nombre;
	private PerfilAbstract PerfilSuperior;
	
}
