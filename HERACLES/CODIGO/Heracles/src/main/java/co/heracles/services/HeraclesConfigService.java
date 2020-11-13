package co.heracles.services;

import java.util.List;

import co.heracles.config.appconfig.ServiceRegistrator;
import co.heracles.entities.ConfiguracionHeracles;

public interface HeraclesConfigService extends ServiceRegistrator {
	public List<ConfiguracionHeracles> findAll();
}