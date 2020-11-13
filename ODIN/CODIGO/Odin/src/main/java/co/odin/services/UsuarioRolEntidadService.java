package co.odin.services;

import java.util.List;

import co.common.entities.Entidad;
import co.common.entities.UsuarioRolEntidad;

public interface UsuarioRolEntidadService {

	public List<UsuarioRolEntidad> findByEntidadAndTipoRol(Entidad entidad,
			String tipoRol);

}
