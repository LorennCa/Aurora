package co.aquiles.services;

import java.util.List;

import co.common.entities.Inventario;

public interface InventarioService {

	void crearProducto(Inventario inventario);

	void actualizarProducto(Inventario inventario);

	List<Inventario> retornaReferencia(String referencia);

}
