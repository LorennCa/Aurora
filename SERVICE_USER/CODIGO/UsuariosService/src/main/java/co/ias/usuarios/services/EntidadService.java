package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.entities.Entidad;


public interface EntidadService {

	/**
	 * Retorna todas las entidades existentes en el sistema
	 * 
	 * @return
	 */
	public List<Entidad> findAll();

	/**
	 * Retorna sólo una entidad por Identificador
	 * 
	 * @param id
	 * @return
	 */
	public Entidad findOne(Integer id);

	public List<Entidad> findByEmisorTrue();

	

	/**
	 * Retorna todas las entidades que sean de tipo Super Administradoras
	 * 
	 * @return
	 */
	public List<Entidad> findBySuperAdminTrue();
}
