package co.ias.usuarios.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.ias.common.entities.Perfil;

public interface PerfilOwnRepository extends JpaRepository<Perfil, Integer> {

	public Perfil findByNombre(String nombre);

	public void delete(Integer id);

	@Query(value = "SELECT p FROM Perfil p WHERE perfilSuperior.id = :idPadre AND p.id NOT IN :idPadre", nativeQuery=true)
	public List<Perfil> findChildren(@Param("idPadre") Integer idPadre);

	public List<Perfil> findByPerfilSuperiorAndIdNotIn(Perfil perfilSuperior,
			List<Integer> ids);
	
	public List<Perfil> findByPerfilSuperior(Perfil perfilSuperio);

	public List<Perfil> findByIdIn(List<Integer> ids);

	public List<Perfil> findByIdNotIn(List<Integer> ids);
	

}
