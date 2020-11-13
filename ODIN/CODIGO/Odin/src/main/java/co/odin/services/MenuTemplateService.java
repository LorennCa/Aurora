package co.odin.services;

import java.util.List;

import co.common.custom.MenuTemplate;
import co.common.entities.Perfil;

public interface MenuTemplateService {

	/**
	 * Retorna los permisos (Nivel 3 de Opciones de Menú)de un perfil y una URI
	 * dada
	 * 
	 * @param perfil
	 * @param uri
	 * @return
	 */
	public List<MenuTemplate> getPermisosByPerfil(Perfil perfil, String uri);
	

}