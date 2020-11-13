package co.teseo.config.appconfig;


import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthInitialConfig extends SpringBootServletInitializer {

	/**
	 * logger de Log4j2 Apache
	 */
	private static final Logger logger = LogManager.getLogger(AuthInitialConfig.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciacion
	 */
	private static MessageSource mrc = MessageSourceConfig.messageSource();


	/**
	 * Método que inicializa la carga de configuración inicial del sistema
	 */
	@PostConstruct
	public void init() {

		initializeConfigTeseo();
		// initializarPruebas();

	}

	/**
	 * PA PRUEBAS
	 */
	public void initializarPruebas() {

	}

	/**
	 * Carga la configuración inicial de sistema, como validación por IP,
	 * conexión con servidor GLuu
	 */
	public void initializeConfigTeseo() {
		logger.info(mrc.getMessage("teseo.initialize.config.info.begin", null,
				null));
	}		
}