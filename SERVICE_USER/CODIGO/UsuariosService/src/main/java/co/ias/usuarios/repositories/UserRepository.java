package co.ias.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;

import co.ias.common.entities.Usuario;

public interface UserRepository extends CrudRepository<Usuario, Integer>{
	
}
