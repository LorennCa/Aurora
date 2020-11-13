package co.odin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.common.entities.Entidad;

public interface EntidadOwnRepository extends JpaRepository<Entidad, Integer> {

	public List<Entidad> findByEmisorTrue();

	public List<Entidad> findByAfiliadoTrue();

	//public List<Entidad> findByScbTrue();

	public List<Entidad> findBySuperAdminTrue();

}
