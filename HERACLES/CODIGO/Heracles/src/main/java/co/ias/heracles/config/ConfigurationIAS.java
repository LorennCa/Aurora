package co.ias.heracles.config;

import java.util.HashMap;

import co.ias.heracles.entities.ConfiguracionIAS;


public class ConfigurationIAS {
	private static ConfigurationIAS instance;
	private static HashMap<String, ConfiguracionIAS> configMap;
	private ConfigurationIAS() {
		configMap = new HashMap<>();
	}
	public static ConfigurationIAS instance() {
		if(instance == null)
			instance = new ConfigurationIAS();
		return instance;
	}
	
	public void addConfiguration(String key, ConfiguracionIAS value) {
		configMap.put(key, value);
	}
	
	public ConfiguracionIAS getConfig() {
		return configMap.get("HERACLES_APP_CONFIGURATION");
	}
}