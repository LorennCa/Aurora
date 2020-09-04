package co.ias.usuarios.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ias.common.entities.Entidad;
import co.ias.common.entities.UsuarioRolEntidad;

public interface UsuarioRolEntidadOwnRepository extends JpaRepository<UsuarioRolEntidad, Integer>{

	public List<UsuarioRolEntidad> findByEntidadAndTipoRol(Entidad entidad, String tipoRol);
}