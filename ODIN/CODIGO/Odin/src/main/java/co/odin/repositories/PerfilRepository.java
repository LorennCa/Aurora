package co.odin.repositories;

import org.springframework.data.repository.CrudRepository;

import co.common.entities.Perfil;

public interface PerfilRepository extends CrudRepository<Perfil, Integer>{

public Perfil findById(Integer id);
	
}
