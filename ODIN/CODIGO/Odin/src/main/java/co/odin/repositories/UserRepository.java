package co.odin.repositories;

import org.springframework.data.repository.CrudRepository;

import co.common.entities.Usuario;

public interface UserRepository extends CrudRepository<Usuario, Integer>{
	
}
