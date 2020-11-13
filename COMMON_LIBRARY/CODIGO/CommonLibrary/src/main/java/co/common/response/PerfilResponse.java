package co.common.response;


import co.common.custom.MenuTemplate;
import co.common.entities.Perfil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PerfilResponse {
	
	private Integer id;
	private String nombre;
	private Perfil perfilSuperior;
	private MenuTemplate opcionesMenu;

	
}
