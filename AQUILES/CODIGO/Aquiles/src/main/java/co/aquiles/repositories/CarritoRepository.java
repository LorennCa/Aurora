package co.aquiles.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.common.entities.Carrito;

public interface CarritoRepository extends CrudRepository<Carrito, Integer>{
	@Modifying
	@Query(value = "INSERT INTO carrito "
			+ "(id_compra, usuario, referencia, color, recurso, precio) "
			+ "VALUES (:id,:usuario,:referencia,:color,:recurso, :precio) ", nativeQuery = true)

	@Transactional
	public void agregaCarrito(
			@Param("id") String id_producto,
			@Param("usuario") String usuario,
			@Param("referencia") String referencia, 
			@Param("color") String color,
			@Param("recurso") String recurso,
			@Param("precio") double precio);
}
