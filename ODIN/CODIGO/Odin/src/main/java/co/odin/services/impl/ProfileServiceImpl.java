package co.odin.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.common.entities.Perfil;
import co.odin.repositories.PerfilOwnRepository;
import co.odin.repositories.PerfilRepository;
import co.odin.services.ProfileService;

@Component
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private PerfilOwnRepository perfilOwnRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Perfil> findAll() {
		return (List<Perfil>) perfilRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Perfil> findAllExclude(List<Integer> ids) {
		return (List<Perfil>) perfilOwnRepository.findByIdNotIn(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Perfil> findAllWithoutMenuOpciones() {
		List<Perfil> perfiles = (List<Perfil>) perfilRepository.findAll();
		for (Perfil perfil : perfiles) {
			perfil.setOpcionesMenu(null);
		}
		return perfiles;
	}

	@Override
	@Transactional(readOnly = true)
	public Perfil findOne(Integer id) {
		return perfilRepository.findOne(id);
	}
	
	@Override
	public Perfil findbyId(Integer id){
		return perfilRepository.findById(id);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Perfil perfil) {
		perfilRepository.save(perfil);

	}

	@Override
	@Transactional(readOnly = true)
	public Perfil findByNombre(String nombre) {
		return perfilOwnRepository.findByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Integer id) {
		perfilOwnRepository.delete(id);
	}

	@Override
	public List<Perfil> findChildren(Integer parentId) {

		// Recorre los hijos directos:
		List<Perfil> children = perfilOwnRepository.findChildren(parentId);
		List<Perfil> allChildren = new ArrayList<Perfil>();

		for (Perfil child : children) {

			allChildren.add(child);

			List<Perfil> grandChildren = perfilOwnRepository.findChildren(child
					.getId());

			if (null != grandChildren && grandChildren.size() != 0) {

				// Recorre los nietos y los agrega (Nivel 3)
				for (Perfil grandChild : grandChildren) {
					allChildren.add(grandChild);
				}
			}
		}

		// Necesita agregar al listado el mismo como perfil
		// En caso tal que no tenga perfiles hijos
		Perfil perfil = perfilRepository.findOne(parentId);
		allChildren.add(perfil);

		return allChildren;
	}

	@Override
	public List<Perfil> findDirectChildren(Integer parentId) {
		List<Perfil> directChildren = perfilOwnRepository
				.findChildren(parentId);
		return directChildren;
	}

	@Override
	public List<Perfil> findByIdIn(List<Integer> ids) {
		return perfilOwnRepository.findByIdIn(ids);
	}

	@Override
	public List<Perfil> findByPerfilSuperiorAndIdNotIn(Perfil perfilSuperior,
			List<Integer> ids) {
		return perfilOwnRepository.findByPerfilSuperiorAndIdNotIn(
				perfilSuperior, ids);
	}

	@Override
	public List<Perfil> findByPerfilSuperior(Perfil perfilSuperior) {
		return perfilOwnRepository.findByPerfilSuperior(perfilSuperior);
	}

}
