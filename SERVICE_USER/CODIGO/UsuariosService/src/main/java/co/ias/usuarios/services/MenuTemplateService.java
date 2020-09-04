package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.Perfil;

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