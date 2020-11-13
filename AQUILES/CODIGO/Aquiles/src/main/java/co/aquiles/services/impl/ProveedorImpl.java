package co.aquiles.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.aquiles.repositories.ProveedorRepository;
import co.aquiles.services.ProveedorService;
import co.common.entities.Proveedor;

@Component
public class ProveedorImpl implements ProveedorService{
	
	@Autowired
	private ProveedorRepository proveedorRepository;
	
	@Override
	public List<Proveedor> retornaProveedor(String proveedor) {
		return proveedorRepository.retornaProveedores(proveedor);
	}
}
