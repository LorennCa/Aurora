package co.ias.usuarios.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ias.common.entities.Entidad;

public interface EntidadOwnRepository extends JpaRepository<Entidad, Integer> {

	public List<Entidad> findByEmisorTrue();

	public List<Entidad> findBySuperAdminTrue();

}
