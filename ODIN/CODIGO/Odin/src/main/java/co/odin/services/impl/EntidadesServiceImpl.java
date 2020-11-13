package co.odin.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import co.common.entities.Entidad;
import co.common.entities.Usuario;
import co.common.response.Entidades;
import co.odin.enums.TipoEntidadEnum;
import co.odin.services.EntidadesService;@Component
public class EntidadesServiceImpl implements EntidadesService {

	@Override
	public Entidades setEntidadesXUsuarioRolEntidad(Usuario usuario) {

		Entidades entidades = new Entidades();
		List<Entidad> entidadList = new ArrayList<Entidad>();
		List<Entidad> entidadListInitialized = new ArrayList<Entidad>();

		entidadList.add(usuario.getUsuarioRolEntidad().getEntidad());

		entidades.setSuperAdministrador(entidadListInitialized);

		if (usuario.getUsuarioRolEntidad().getTipoRol()
				.equalsIgnoreCase(TipoEntidadEnum.EMISOR.name())) {

			entidades.setEmisores(entidadList);
			entidades.setAfiliados(entidadListInitialized);
			entidades.setScb(entidadListInitialized);

		} else if (usuario.getUsuarioRolEntidad().getTipoRol()
				.equalsIgnoreCase(TipoEntidadEnum.AFILIADO.name())) {

			entidades.setEmisores(entidadListInitialized);
			entidades.setAfiliados(entidadList);
			entidades.setScb(entidadListInitialized);

		} else if (usuario.getUsuarioRolEntidad().getTipoRol()
				.equalsIgnoreCase(TipoEntidadEnum.SCB.name())) {

			entidades.setEmisores(entidadListInitialized);
			entidades.setAfiliados(entidadListInitialized);
			entidades.setScb(entidadList);
		}

		return entidades;
	}
}