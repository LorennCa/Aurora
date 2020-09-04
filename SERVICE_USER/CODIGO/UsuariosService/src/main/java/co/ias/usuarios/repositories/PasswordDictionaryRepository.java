package co.ias.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;

import co.ias.common.entities.DiccionarioClaves;

public interface PasswordDictionaryRepository extends CrudRepository<DiccionarioClaves, Integer>{

}