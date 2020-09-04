package co.ias.usuarios.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.entities.Entidad;
import co.ias.common.entities.UsuarioRolEntidad;
import co.ias.usuarios.repositories.UsuarioRolEntidadOwnRepository;
import co.ias.usuarios.services.UsuarioRolEntidadService;


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
