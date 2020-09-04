package co.ias.aurora.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.aurora.repositories.InventarioRepository;
import co.ias.aurora.services.InventarioService;
import co.ias.common.entities.Carrito;
import co.ias.common.entities.Inventario;

@Component
public class InventarioImpl implements InventarioService{
	
	@Autowired
	private InventarioRepository inventarioRepository;
	
	@Override
	public List<Inventario> findAll(){
		return (List<Inventario>) inventarioRepository.findAll();
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
				inventario.getRecurso(), 
				inventario.getPalabra_clave());
		
	}
	
}
