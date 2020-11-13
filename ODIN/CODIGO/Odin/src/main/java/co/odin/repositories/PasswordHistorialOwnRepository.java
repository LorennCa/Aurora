package co.odin.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.common.entities.PasswordHistorial;

public interface PasswordHistorialOwnRepository extends JpaRepository<PasswordHistorial, Integer>{

	@Query(value = "SELECT a.password FROM usuario_password_historial a "
			+ "WHERE a.cod_usuario = :idUsuario ORDER BY a.fecha desc limit 1", nativeQuery = true)
	public String findByUsuario(@Param("idUsuario") Integer id_Usuario);
	
}
