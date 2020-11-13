package co.odin.config.appconfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.MessageSource;

import co.common.custom.MenuTemplate;
import co.common.entities.ConfiguracionAurora;
import co.common.entities.Menu;
import co.common.entities.Perfil;
import co.odin.enums.TipoMenuEnum;
import co.odin.services.MenuService;
import co.odin.services.ProfileService;
import co.odin.utils.AppConstants;
import co.odin.utils.HermesMachine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AppConfiguration {
	private static AppConfiguration instance;
	public static AppConfiguration intance() {
		if(instance == null)
			instance = new AppConfiguration();
		return instance;
	}
	
	private AppConfiguration(){}
	/**
	 * Map que contendrá: <K> -> constante de configuración <V> -> String con el
	 * valor asociado en base de datos
	 */
	private Map<String, ConfiguracionAurora> configuracionOdinMap = new HashMap<String, ConfiguracionAurora>();
	
	private List<Perfil> profiles = new ArrayList<Perfil>();
	
	/**
	 * Map que contendrá: <K> -> constante de configuración <V> -> String con el
	 * valor asociado en base de datos
	 */
	private Map<Integer, MenuTemplate> menuGeneralXPerfilMap = new HashMap<>();
	
	private Map<Integer, List<MenuTemplate>> permisosPerfilXPerfilMap = new HashMap<>();
	
	private Map<Integer, MenuTemplate> menuAllxPerfilToUpdate = new HashMap<>();

	/**
	 * Map que contiene por clave los días festivos para la validaciones de
	 * negocio en el IAS
	 */
	public static Map<String, String> holidaysOdinMap = new HashMap<String, String>();

	private boolean configured = false;
	private static final Logger logger = LogManager.getLogger(AppConfiguration.class);
	private static MessageSource mrc = MessageSourceConfig.messageSource();
	
	public void initializeSEEConfig(ConfiguracionAurora seeConfig) {
		if(!configured) {
			configuracionOdinMap.put(AppConstants.General.ODIN_APPLICATION_CONFIGURATION, seeConfig);
			configured = true;
		}
	}

	public ConfiguracionAurora getSeeConfig() {
		return configuracionOdinMap.get(AppConstants.General.ODIN_APPLICATION_CONFIGURATION);
	}

	public void initializeData(MenuService menuService, ProfileService profileService) throws Exception {
		initializeMenuPerProfile(profileService);
		initializeProfilePrivilegesOverProfile(menuService);
		initializeMenuPerProfileForUpdate(menuService);
		initializeHolidays();
	}

	private void initializeHolidays() throws Exception {
		try {

			logger.info(mrc.getMessage("odin.initialize.holidays.info.begin",null, null));

			/**
			 * Lectura del archivo de JSON (parser to Festivo class)
			 */
			JSONParser parser = new JSONParser();

			final String APP_ARGUMENTS_FILE_PATH = "/auroraConfigFiles/festivos.json";
			Object obj = parser.parse(new FileReader(APP_ARGUMENTS_FILE_PATH));

			// Carga del objeto y el array de festivos
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray holidayJSON = (JSONArray) jsonObject.get("festivos");

			for (Object keyValue : holidayJSON)
				holidaysOdinMap.put(keyValue.toString(),	keyValue.toString());
			
			if (holidaysOdinMap.size() > 0)
				logger.info(mrc.getMessage("odin.initialize.holidays.info.end",null, null));
			else
				throw new Exception("Hubo un error al cargar los festivos");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void initializeMenuPerProfileForUpdate(MenuService menuService) {
		logger.info(mrc.getMessage("odin.initialize.profiles.opcionesMenuxPerfilToUpdate.begin.info",
						null, null));
		try {
			List<Menu> todasOpcionesMenu = menuService.findByTipoMenuNot(TipoMenuEnum.CONSULTA.name());
			if (todasOpcionesMenu != null && todasOpcionesMenu.size() != 0) {
				/*
				 * Se recorre el listado de TODOS los perfiles para ir
				 * insertando en el Map TODAS las opciones de Menú, si no está
				 * asignada la opción de Menú al perfil, el estado del menú es =
				 * false
				 */
				for (Perfil perfil : profiles) {
					menuAllxPerfilToUpdate.put(perfil.getId(), HermesMachine.construirMenuToUpdateOrCreatePerfil(
											todasOpcionesMenu,
											perfil.getOpcionesMenu()));
				}
			}
		} catch (Exception e) {
			logger.error(mrc.getMessage("odin.initialize.profiles.opcionesMenuxPerfilToUpdate.error", null, null) 
						 + e.getMessage());
		}
		logger.info(mrc.getMessage("odin.initialize.profiles.opcionesMenuxPerfilToUpdate.end.info", null, null));
	}

	private void initializeProfilePrivilegesOverProfile(MenuService menuService) {
		logger.info(mrc.getMessage("odin.initialize.profiles.permisosPerfilParaPerfil.begin", null, null));

		try {
			List<Menu> todasOpcionesMenu = menuService.findAll();
			if (todasOpcionesMenu != null && todasOpcionesMenu.size() != 0) {
				/*
				 * se recorre el listado de TODOS los perfiles para ir
				 * insertando en el Map los permisos que se tienen:
				 */
				for (Perfil perfil : profiles) {
					permisosPerfilXPerfilMap.put(perfil.getId(), 
												 HermesMachine.construirPermisosPerfilParaPerfil(
														 todasOpcionesMenu, perfil.getOpcionesMenu()));

				}
			}
		} catch (Exception e) {
			logger.error(mrc.getMessage("odin.initialize.profiles.permisosPerfilParaPerfil.error", null, null)
					+ e.getMessage());
		}
		logger.info(mrc.getMessage("odin.initialize.profiles.permisosPerfilParaPerfil.end", null, null));
	}

	private void initializeMenuPerProfile(ProfileService profileService) {
		logger.info(mrc.getMessage("odin.initialize.profiles.opcionesMenu.begin.info", null, null));

		try {
			profiles = profileService.findAll();
			if (profiles != null && profiles.size() != 0) {
				
				profiles.stream().forEach(pro -> menuGeneralXPerfilMap.put(pro.getId(), 
								  HermesMachine.construirMenuGeneral(pro.getOpcionesMenu())));
				
				/*for (Perfil perfil : profiles) {
					MenuTemplate mt = HermesMachine.construirMenuGeneral(perfil.getOpcionesMenu());
					if (mt != null)
						menuGeneralXPerfilMap.put(perfil.getId(), mt);
					else
						logger.error("Perfil: " + perfil.getNombre() + mrc.getMessage(
									 "odin.initialize.profiles.opcionesMenu.empty.info", null, null));
				}*/
			}
		} catch (Exception e) {
			logger.error(mrc.getMessage("odin.initialize.profiles.opcionesMenu.error", null, null)
					+ e.getMessage());
		}
		logger.info(mrc.getMessage("odin.initialize.profiles.opcionesMenu.end.info", null, null));
	}
}
