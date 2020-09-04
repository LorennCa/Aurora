package co.ias.heracles.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.heracles.entities.ConfiguracionIAS;
import co.ias.heracles.repositories.ConfiguracionIASRepository;
import co.ias.heracles.services.IASConfigService;

@Component
public class ConfigServiceImpl implements IASConfigService {
	
	@Autowired
	private ConfiguracionIASRepository repo;

	@Override
	public List<ConfiguracionIAS> findAll() {
		return repo.findAll();
	}	
}