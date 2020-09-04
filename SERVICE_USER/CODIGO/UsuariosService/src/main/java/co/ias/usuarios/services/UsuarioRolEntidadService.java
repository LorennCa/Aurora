package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.entities.Entidad;
import co.ias.common.entities.UsuarioRolEntidad;

public interface UsuarioRolEntidadService {

	public List<UsuarioRolEntidad> findByEntidadAndTipoRol(Entidad entidad,
			String tipoRol);

}
