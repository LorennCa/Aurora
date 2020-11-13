package co.odin.repositories;

import org.springframework.data.repository.CrudRepository;

import co.common.entities.DiccionarioClaves;

public interface PasswordDictionaryRepository extends CrudRepository<DiccionarioClaves, Integer>{

}