package co.ias.common.response;


import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.Perfil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PerfilResponse {
	
	private Integer id;
	private String nombre;
	private Perfil perfilSuperior;
	private MenuTemplate opcionesMenu;

	
}
