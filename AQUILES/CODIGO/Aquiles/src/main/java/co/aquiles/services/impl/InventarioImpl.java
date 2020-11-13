package co.aquiles.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.aquiles.repositories.InventarioRepository;
import co.aquiles.services.InventarioService;
import co.common.entities.Carrito;
import co.common.entities.Inventario;

@Component
public class InventarioImpl implements InventarioService{
	
	@Autowired
	private InventarioRepository inventarioRepository;
	
	@Override
	public List<Inventario> retornaReferencia(String referencia){
		return inventarioRepository.retornaReferencia(referencia);
	}
	
	@Override
	public void crearProducto(Inventario inventario){
		inventarioRepository.save(inventario);
	}
	
	@Override
	public void actualizarProducto(Inventario inventario){
		inventarioRepository.actualizarProducto(inventario.getClave().getId_producto(), 
				inventario.getClave().getReferencia(), 
				inventario.getClave().getColor(), 
				inventario.getPrecio(), 
				inventario.getCantidad(), 
				inventario.getRecursoUno(), 
				inventario.getPalabra_clave());
		
	}
	
}
