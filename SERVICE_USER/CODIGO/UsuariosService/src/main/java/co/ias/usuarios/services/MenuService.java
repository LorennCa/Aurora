package co.ias.usuarios.services;

import java.util.List;
import java.util.Set;

import co.ias.common.entities.Menu;
import co.ias.common.entities.Perfil;

public interface MenuService {

	/**
	 * Encuentra todas las opciones de Menú
	 * 
	 * @return
	 */
	public List<Menu> findAll();

	/**
	 * Encuentra TODAS las opciones de Menú, excepto las que son de consulta (No
	 * se mostrarán en la administración de opciones de Menu de perfiles)
	 * 
	 * @param tipoMenu
	 * @return
	 */
	public List<Menu> findByTipoMenuNot(String tipoMenu);

	/**
	 * Encuentra una Opción de Menú por Id
	 * 
	 * @return
	 */
	public Menu findOne(Integer id);

	/**
	 * Encuentra todas las opciones de Menú de una funcionalidad, tipo Menú o
	 * Nivel
	 * 
	 * @param nivel
	 * @param menuPadre
	 * @return
	 */
	public List<Menu> findUriServicioContainingTipoMenuOrNivel(String uri,
			String tipoMenu, Integer nivel);

	/**
	 * Retorna Menu por ids
	 * 
	 * @param ids
	 * @return
	 */
	public List<Menu> findByIdIn(Set<Integer> ids);

	/**
	 * Agrega una lista de opcones de menú al perfil que entra como parámetro
	 * 
	 * @param perfil
	 * @param menuList
	 */
	public void insertNewMenuToPerfil(Perfil perfil, List<Menu> menuList);

	/**
	 * Retorna las opciones de menú (permisos) dado un menu padre
	 * 
	 * @param menu
	 * @return
	 */
	public List<Menu> findByMenuPadre(Menu menu);

}
