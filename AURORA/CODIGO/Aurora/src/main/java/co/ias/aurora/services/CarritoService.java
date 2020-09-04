package co.ias.aurora.services;

import java.util.List;

import co.ias.common.entities.Carrito;

public interface CarritoService {

	void agregarCarrito(Carrito producto);

	List<Carrito> retornaCarrito(String login);

}
