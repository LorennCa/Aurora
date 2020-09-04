package co.ias.heracles.config.appconfig;

import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import co.ias.heracles.config.ConfigurationIAS;
import co.ias.heracles.entities.ConfiguracionIAS;
import co.ias.heracles.services.IASConfigService;

@Configuration
public class InitialConfig extends SpringBootServletInitializer {

	/**
	 * logger de Log4j2 Apache
	 */
	private static final Logger logger = LogManager.getLogger(InitialConfig.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplicación.
	 */
	private static MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private IASConfigService configService;
	
	@Autowired
	private Environment springEnv;


	/**
	 * Método que inicializa la carga de configuración inicial del sistema
	 */
	@PostConstruct
	public void init() {
		initializeServiceContext();
		try {
			registerItself();			
		} catch (Exception e) {
			logger.error("Excepción registrando el servicio en Perseo\n"+e.getLocalizedMessage());
			return;
		}
	}
	/**
	 * Carga la configuración inicial de sistema
	 */
	public void initializeServiceContext() {
		logger.info(mrc.getMessage("ias.initialize.config.info.begin", null, null));
		List<ConfiguracionIAS> config = configService.findAll();

		if (config != null && config.size() > 0)
			ConfigurationIAS.instance().addConfiguration("HERACLES_APP_CONFIGURATION", config.get(0));
		else
			logger.error(mrc.getMessage("ias.initialize.config.info.error",
					null, null));		
		logger.info("\nConfiguración: " + ConfigurationIAS.instance().getConfig()+"\n"+ AppInitParams.instance());
		logger.info(mrc.getMessage("ias.initialize.config.info.end", null, null));
	}
	
	public String registerItself() throws Exception {
		logger.info("Service about to register itself to the Perseo: " + configService);
		configService.registerItself(springEnv, logger);
		return null;
	}		
}