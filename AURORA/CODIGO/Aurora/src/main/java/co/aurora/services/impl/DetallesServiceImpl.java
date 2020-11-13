package co.aurora.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.aurora.repositories.InventarioRepository;
import co.aurora.services.DetallesService;
import co.common.entities.Inventario;

@Component
public class DetallesServiceImpl implements DetallesService{
	@Autowired
	private InventarioRepository inventarioRepository;
	
	@Override
	public Inventario retornaDetalles(String producto) {
		System.out.println(inventarioRepository.retornaDetalle(producto).getClave().getId_producto());
		return inventarioRepository.retornaDetalle(producto);
	}
}
