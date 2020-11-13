package co.aurora.services;

import java.util.List;

import co.common.entities.Inventario;

public interface InventarioService {

	List<Inventario> findAll();

	void crearProducto(Inventario inventario);

	void actualizarProducto(Inventario inventario);

}
