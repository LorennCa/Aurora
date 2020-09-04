package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.entities.TipoDocumento;


public interface TipoDocumentoService {
	
	public List<TipoDocumento> findAll();

	public TipoDocumento findOne(Integer id);
}
