package co.ias.usuarios.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.entities.TipoDocumento;
import co.ias.usuarios.repositories.TipoDocumentoRepository;
import co.ias.usuarios.services.TipoDocumentoService;

@Component
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Override
	public List<TipoDocumento> findAll() {
		return (List<TipoDocumento>) tipoDocumentoRepository.findAll();
	}

	@Override
	public TipoDocumento findOne(Integer id) {
		return tipoDocumentoRepository.findOne(id);
	}

}
