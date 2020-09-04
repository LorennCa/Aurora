package co.ias.aurora.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.ias.common.entities.Inventario;


public interface InventarioRepository extends CrudRepository<Inventario, String>{
	
	@Modifying
	@Query(value = "UPDATE inventario " + "SET precio= :precio,  cantidad= :cantidad, recurso=:recurso, palabra_clave=:palabra_clave "
			+ "WHERE id = :id_producto and referencia = :referencia and color =:color", nativeQuery = true)
	@Transactional
	public void actualizarProducto(
			@Param("id_producto") String id_producto,
			@Param("referencia") String referencia,
			@Param("color") String color,
			@Param("precio") double precio, 
			@Param("cantidad") int cantidad,
			@Param("recurso") String recurso,
			@Param("palabra_clave") String palabra_clave);

}
