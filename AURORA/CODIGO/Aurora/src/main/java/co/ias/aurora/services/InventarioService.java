package co.ias.aurora.services;

import java.util.List;

import co.ias.common.entities.Inventario;

public interface InventarioService {

	List<Inventario> findAll();

	void crearProducto(Inventario inventario);

	void actualizarProducto(Inventario inventario);

}
