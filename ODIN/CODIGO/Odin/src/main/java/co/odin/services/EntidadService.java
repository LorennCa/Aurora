package co.odin.services;

import java.util.List;

import co.common.entities.Entidad;


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

	/**
	 * Retorna todas las entidades que sean de tipo emisoras
	 * 
	 * @return
	 */
	//public List<Entidad> findByEmisorTrue();

	/**
	 * Retorna todas las entidades que sean de tipo Afiliado al Mercado
	 * Electrónico Colombiano (MEC)
	 * 
	 * @return
	 */
	//public List<Entidad> findByAfiliadoTrue();

	/**
	 * Retorna todas las entidades que sean de tipo Sociedad Comisionista de
	 * Bolsa
	 * 
	 * @return
	 */
	//public List<Entidad> findByScbTrue();

	/**
	 * Retorna todas las entidades que sean de tipo Super Administradoras
	 * 
	 * @return
	 */
	public List<Entidad> findBySuperAdminTrue();
}
