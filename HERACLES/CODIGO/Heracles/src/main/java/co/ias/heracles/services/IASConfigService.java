package co.ias.heracles.services;

import java.util.List;

import co.ias.heracles.config.appconfig.ServiceRegistrator;
import co.ias.heracles.entities.ConfiguracionIAS;

public interface IASConfigService extends ServiceRegistrator {
	public List<ConfiguracionIAS> findAll();
}