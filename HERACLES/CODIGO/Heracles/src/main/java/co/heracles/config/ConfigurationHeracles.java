package co.heracles.config;

import java.util.HashMap;

import co.heracles.entities.ConfiguracionHeracles;


public class ConfigurationHeracles {
	private static ConfigurationHeracles instance;
	private static HashMap<String, ConfiguracionHeracles> configMap;
	private ConfigurationHeracles() {
		configMap = new HashMap<>();
	}
	public static ConfigurationHeracles instance() {
		if(instance == null)
			instance = new ConfigurationHeracles();
		return instance;
	}
	
	public void addConfiguration(String key, ConfiguracionHeracles value) {
		configMap.put(key, value);
	}
	
	public ConfiguracionHeracles getConfig() {
		return configMap.get("HERACLES_APP_CONFIGURATION");
	}
}