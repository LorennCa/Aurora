package co.teseo.repository;

import org.springframework.data.repository.CrudRepository;

import co.teseo.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {
	
}