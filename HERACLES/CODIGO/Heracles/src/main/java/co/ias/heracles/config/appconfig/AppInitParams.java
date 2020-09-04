/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.heracles.config.appconfig;

import java.io.File;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppInitParams {
	private static AppInitParams instance;
	private static final Logger logger = LogManager.getLogger(AppInitParams.class);
	private static Configuration argConfig = null;
    public static final String APP_ARGUMENTS_FILE_PATH = "/iasConfigFiles/appArgs.properties";
    private static boolean argRefreshed;
	
    {
    	initializeArgs(true);
    }
    
    private AppInitParams() {
    }
    
    public static AppInitParams instance() {
    	if(instance == null)
    		instance = new AppInitParams();
    	return instance;
    }
    
	/**
	 * Retorna un parámetro de configuración de la aplicación dada su llave.
	 * 
	 * @param key
	 * @return
	 */
	private String getConfiguration(String key) {
		try {
			if (argRefreshed) {
				if (key.indexOf("time", 0) >= 0) {
					logger.info("<in-memory access> key_int_value: " + argConfig.getInt(key));
					return String.valueOf(argConfig.getInt(key));
				} else {
					logger.info("<in-memory access> key_string_value: " + argConfig.getString(key));
					return argConfig.getString(key);
				}
			} else {
				File file1 = new File("/iasConfigFiles/appArgs.properties");
				logger.info("<File access> " + file1.getAbsolutePath());
				argConfig = new Configurations().properties(file1);
				argRefreshed = true;
				if (key.indexOf("time", 0) >= 0) {
					logger.info("key_int_value: " + argConfig.getInt(key));
					return String.valueOf(argConfig.getInt(key));
				} else {
					logger.info("key_string_value: " + argConfig.getString(key));
					return argConfig.getString(key);
				}
			}
		} catch (ConfigurationException ex) {
			logger.error(ex.getLocalizedMessage());
			return "<no_value>";
		}

	}

	public void initializeArgs(boolean refresh) {
		if (refresh) {
			try {
				File argFile = new File(APP_ARGUMENTS_FILE_PATH);
				argConfig = new Configurations().properties(argFile);
				logger.info("Application parameters loaded: " + argFile.getAbsolutePath());
				argRefreshed = true;
			} catch (org.apache.commons.configuration2.ex.ConfigurationException cex) {
				logger.error(cex.getLocalizedMessage());
			}
			return;
		}
		argRefreshed = false;
	}

	public String getCaptcha() {
		return getConfiguration("captcha");
	}
	
	public String getBannerPicture() {
		return getConfiguration("bannerPicture");
	}
	
	public Long getTiempoSessionActiva() {
		return Long.parseLong(getConfiguration("sessionTimeOut"));
	}
	
	public Integer getIntentosPermitidosLogin() {
		return Integer.parseInt(getConfiguration("intentosPermitidos"));
	}
}