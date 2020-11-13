package co.heracles.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.heracles.entities.ConfiguracionHeracles;
import co.heracles.repositories.ConfiguracionHeraclesRepository;
import co.heracles.services.HeraclesConfigService;

@Component
public class ConfigServiceImpl implements HeraclesConfigService {
	
	@Autowired
	private ConfiguracionHeraclesRepository repo;

	@Override
	public List<ConfiguracionHeracles> findAll() {
		return repo.findAll();
	}	
}