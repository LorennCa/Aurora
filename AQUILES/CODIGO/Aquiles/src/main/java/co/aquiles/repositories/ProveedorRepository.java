package co.aquiles.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.common.entities.Proveedor;

public interface ProveedorRepository extends CrudRepository<Proveedor, Integer>{
	
	
	@Query(value = "SELECT p.* FROM  Proveedor p WHERE p.nombre_proveedor like %:proveedor%", nativeQuery = true)
	public List<Proveedor> retornaProveedores(
			@Param("proveedor")String proveedor);
}
