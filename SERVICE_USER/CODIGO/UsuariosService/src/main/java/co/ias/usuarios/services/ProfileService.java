package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.entities.Perfil;


public interface ProfileService {

	/**
	 * List de Perfiles
	 * 
	 * @return
	 */
	public List<Perfil> findAll();

	/**
	 * Retorna todos los usuarios exluyendo los pasados por id
	 * 
	 * @param ids
	 * @return
	 */
	public List<Perfil> findAllExclude(List<Integer> ids);

	/**
	 * Retorna la lista de perfiles sin opciones de Meú
	 * 
	 * @return
	 */
	public List<Perfil> findAllWithoutMenuOpciones();

	/**
	 * Busca un Perfil por Id
	 * 
	 * @param id
	 * @return
	 */
	public Perfil findOne(Integer id);

	/**
	 * Guarda un perfil dado como parámetro
	 * 
	 * @param perfil
	 */
	public void save(Perfil perfil);

	/**
	 * Retorna un perfil por nombre
	 * 
	 * @param name
	 * @return
	 */
	public Perfil findByNombre(String name);

	/**
	 * Elimina un perfil oir identificador
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * encuentra Perfil padre
	 * 
	 * @param id
	 */
	public List<Perfil> findChildren(Integer parentId);

	/**
	 * Encuentra todos los hijos directos dados un id Padre
	 * 
	 * @return
	 */
	public List<Perfil> findDirectChildren(Integer parentId);

	/**
	 * Encuentra los perfiles que tengan el mismo perfil padre Excluye los ids
	 * que entren como parámetro
	 * 
	 * @param perfilSuperior
	 * @param ids
	 * @return
	 */
	public List<Perfil> findByPerfilSuperiorAndIdNotIn(Perfil perfilSuperior,
			List<Integer> ids);

	/**
	 * Retorna un listado de perfiles por Id
	 * 
	 * @param ids
	 * @return
	 */
	public List<Perfil> findByIdIn(List<Integer> ids);

	/**
	 * Retorna todos los perfiles que tengan un mismo perfil padre
	 * 
	 * @param perfilSuperior
	 * @return
	 */
	public List<Perfil> findByPerfilSuperior(Perfil perfilSuperior);

	public Perfil findbyId(Integer id);
}
