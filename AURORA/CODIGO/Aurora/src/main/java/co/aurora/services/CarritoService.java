package co.aurora.services;

import java.util.List;

import co.common.entities.Carrito;

public interface CarritoService {

	void agregarCarrito(Carrito producto);

	List<Carrito> retornaCarrito(String login);

}
