package co.ias.heracles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ias.heracles.entities.ConfiguracionIAS;

public interface ConfiguracionIASRepository extends
		JpaRepository<ConfiguracionIAS, Integer> {

}
