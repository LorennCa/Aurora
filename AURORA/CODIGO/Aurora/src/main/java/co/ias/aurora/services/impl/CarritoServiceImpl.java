package co.ias.aurora.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.aurora.repositories.CarritoRepository;
import co.ias.aurora.services.CarritoService;
import co.ias.common.entities.Carrito;

@Component
public class CarritoServiceImpl implements CarritoService{
	
	@Autowired
	private CarritoRepository carritoRepository;
	
	@Override
	public void agregarCarrito(Carrito producto){
		System.out.println(producto.getReferencia());
		System.out.println(producto.getPrecio());
		carritoRepository.agregaCarrito(producto.getId(),producto.getUsuario(), producto.getReferencia(), producto.getColor(), producto.getRecurso(), producto.getPrecio());
	}
	
	@Override
	public List<Carrito> retornaCarrito(String login){
		return (List<Carrito>) carritoRepository.findAll();
	}

}
