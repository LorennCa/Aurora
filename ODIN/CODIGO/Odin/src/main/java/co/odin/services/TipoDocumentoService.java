package co.odin.services;

import java.util.List;

import co.common.entities.TipoDocumento;


public interface TipoDocumentoService {
	
	public List<TipoDocumento> findAll();

	public TipoDocumento findOne(Integer id);
}
