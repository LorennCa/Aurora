package co.odin.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import co.common.entities.Menu;

import org.springframework.data.jpa.repository.Query;

public interface MenuOwnRepository extends JpaRepository<Menu, Integer> {

	public List<Menu> findByNivelAndUriServicioContaining(Integer nivel,
			String uri);

	@Query(value = "Select * from menu m where m.uri_servicio like (%:uri%) and (m.tipo_menu = :tipoMenu or m.nivel = :nivel) order by orden", nativeQuery = true)
	public List<Menu> findUriServicioContainingTipoMenuOrNivel(
			@Param("uri") String uri, @Param("tipoMenu") String tipoMenu,
			@Param("nivel") Integer nivel);

	public List<Menu> findByIdIn(Set<Integer> ids);

	public List<Menu> findByMenuPadre(Menu menuPadre);

	public List<Menu> findByTipoMenuNot(String tipoMenu);

	@Query(value = "SELECT menu.* "
			+ "FROM menu menu "
			+ "INNER JOIN menu_perfil menu_per ON (menu_per.cod_menu = menu.id) "
			+ "WHERE menu_per.cod_perfil = :perfilId", nativeQuery = true)
	public List<Menu> findByPerfil(@Param("perfilId") Integer perfilId);

	public List<Menu> findByTipoMenu(String tipoMenu);
	
	
}
