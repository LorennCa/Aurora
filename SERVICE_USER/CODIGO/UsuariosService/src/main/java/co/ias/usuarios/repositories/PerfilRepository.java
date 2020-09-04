package co.ias.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;

import co.ias.common.entities.Perfil;

public interface PerfilRepository extends CrudRepository<Perfil, Integer>{

public Perfil findById(Integer id);
	
}
