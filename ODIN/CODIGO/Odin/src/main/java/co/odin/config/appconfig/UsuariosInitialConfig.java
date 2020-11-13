package co.odin.config.appconfig;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import co.common.entities.ConfiguracionAurora;
import co.odin.config.ServicesConfig;
import co.odin.config.appconfig.MailConfiguration;
import co.odin.dtos.ServiceDTO;
import co.odin.perseo.inmemory.ServicesMap;
import co.odin.services.CerberoService;
import co.odin.services.MenuService;
import co.odin.services.ProfileService;
import co.odin.utils.HermesMachine;

@Configuration
public class UsuariosInitialConfig extends SpringBootServletInitializer {
	@Autowired
	private ServicesConfig services;
	/**
	 * logger de Log4j2 Apache
	 */
	private static final Logger logger = LogManager.getLogger(UsuariosInitialConfig.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private static MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private CerberoService cancerbero;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private Environment springEnv;
	
	@Autowired
	private MailConfiguration mailconf;
	
//	@Autowired
//	private MailConfiguration2 mailconf2;
//	
//	@Autowired 
//	private MailUtilService mailUtil;


	/**
	 * Método que inicializa la carga de configuración inicial del sistema
	 */
	@PostConstruct
	public void init() {
		try {
			initilizeHolidays();
			initializeServiceContext();
			registerItself();
			initializeServiceContextGW();
		} catch (Exception e) {
			logger.warn("\n\n**Excepción registrando el servicio en el Service Registry\n"
						+ e.getLocalizedMessage() + "\n**");
			return;
		}
	}
	
	/**
	 * Inicializa el contexto funcional del servicio
	 * @throws Exception
	 */
	public void initializeServiceContext() throws Exception {
		logger.info(mrc.getMessage("odin.initialize.config.info.begin", null, null));
		String serviceUrl = HermesMachine.getServiceURL("configService","seeconfig", springEnv);
		AppConfiguration.intance().initializeSEEConfig((ConfiguracionAurora)HermesMachine
						.consumeRestService(MediaType.TEXT_PLAIN, serviceUrl, HttpMethod.GET,
											ConfiguracionAurora.class, null).getBody());
		AppConfiguration.intance().initializeData(menuService, profileService);
		
		/*
		 * Obtiene la configuracion inicial para el envio de correo en Reset Password
		 */		
		HermesMachine.getMailConfig(springEnv, mailconf);
		//mailUtil.getMailConfig(springEnv, mailconf2);
	}
	
	public String registerItself() throws Exception {
		logger.info("Service about to register itself to the Service Registry: " + cancerbero);
		return cancerbero.registerItself(springEnv, logger);
	}
	
	/**
	 * Carga los registros de la tabla de festivos de la base de datos para
	 * tenerla en un Map de consulta
	 * 
	 * @throws Exception
	 */
	private void initilizeHolidays() throws Exception {

		try {

			logger.info(mrc.getMessage("odin.initialize.holidays.info.begin",
					null, null));

			/**
			 * Lectura del archivo de JSON (parser to Festivo class)
			 */
			JSONParser parser = new JSONParser();

			final String APP_ARGUMENTS_FILE_PATH = "/auroraConfigFiles/festivos.json";
			Object obj = parser.parse(new FileReader(APP_ARGUMENTS_FILE_PATH));

			// Carga del objeto y el array de festivos
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray holidayJSON = (JSONArray) jsonObject.get("festivos");

			for (Object keyValue : holidayJSON) {
				AppConfiguration.holidaysOdinMap.put(keyValue.toString(),
						keyValue.toString());
			}

			if (AppConfiguration.holidaysOdinMap.size() > 0) {
				logger.info(mrc.getMessage("odin.initialize.holidays.info.end",
						null, null));
			} else {
				throw new Exception(
						"Hubo un error al cargar los festivos en el SEE");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	private String synchronizeServices() throws Exception {
		logger.info(mrc.getMessage("odin.initialize.config.info.begin", null, null));
		String serviceUrl = services.getServices().get(0).getServiceURL();
		logger.info("attempting to get services from Service Registry at olaolaola: " + serviceUrl);		
		ResponseEntity<?> responseEntity = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrl, 
						  HttpMethod.GET, new ParameterizedTypeReference<List<ServiceDTO>>() {}, null);
		List<ServiceDTO> services = (List<ServiceDTO>) responseEntity.getBody();
		ServicesMap.instance().addServicesAtInitialization(services);		
		logger.info("service registry response: " + responseEntity);
		return responseEntity.toString();
	}
	
	/**
	 * Carga la configuración inicial de sistema, como validación por IP,
	 * conexión con servidor GLuu
	 * @throws Exception 
	 */
	public void initializeServiceContextGW() throws Exception {
		synchronizeServices();
	}
}