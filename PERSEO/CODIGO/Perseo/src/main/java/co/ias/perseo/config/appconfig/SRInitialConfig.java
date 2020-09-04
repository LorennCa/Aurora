package co.ias.perseo.config.appconfig;


import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import co.ias.perseo.dtos.ServiceDTO;
import co.ias.perseo.inmemory.ServicesMap;
import co.ias.perseo.service.PerseoService;

@Configuration
public class SRInitialConfig extends SpringBootServletInitializer {

	private static final Logger logger = LogManager.getLogger(SRInitialConfig.class);

	private static MessageSource mrc = MessageSourceConfig.messageSource();

	@Autowired
	private PerseoService srservice;
	
	@PostConstruct
	public void init() {
		initializeServiceContext();
	}
	/**
	 * Cargar la configuración inicial de sistema, como validación por IP,
	 */
	public void initializeServiceContext() {
		logger.info(mrc.getMessage("ias.initialize.config.info.begin", null, null));
		List<ServiceDTO> services = srservice.getAllServices();
		ServicesMap.instance().addServicesAtInitialization(services);
	}
}