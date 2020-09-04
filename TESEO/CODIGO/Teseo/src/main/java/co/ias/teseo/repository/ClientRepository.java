package co.ias.teseo.repository;

import org.springframework.data.repository.CrudRepository;

import co.ias.teseo.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {
	
}