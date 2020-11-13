package co.odin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.common.entities.Entidad;
import co.common.entities.UsuarioRolEntidad;
import co.odin.repositories.UsuarioRolEntidadOwnRepository;
import co.odin.services.UsuarioRolEntidadService;


@Component
public class UsuarioRolEntidadServiceImpl implements UsuarioRolEntidadService {

	@Autowired
	private UsuarioRolEntidadOwnRepository usuarioRolentidadOwnRepository;

	@Override
	public List<UsuarioRolEntidad> findByEntidadAndTipoRol(Entidad entidad,
			String tipoRol) {
		return usuarioRolentidadOwnRepository.findByEntidadAndTipoRol(entidad,
				tipoRol);
	}

}
