/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.odin.utils;

import static co.common.utils.CommonAppConstants.General.CAPITAL_CHARS;
import static co.common.utils.CommonAppConstants.General.NUMBERS;
import static co.common.utils.CommonAppConstants.General.OTP_DIGIT_NUMBER;
import static co.common.utils.CommonAppConstants.General.SMALL_CHARS;
import static co.common.utils.CommonAppConstants.General.SYMBOLS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.google.gson.Gson;

import co.common.custom.MenuTemplate;
import co.common.entities.ConfiguracionAurora;
import co.common.entities.Menu;
import co.common.response.RResponse;
import co.common.response.ResourceResponse;
import co.common.response.Response;
import co.odin.config.appconfig.MailConfiguration;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.dtos.ServiceDTO;
import co.odin.enums.TipoMenuEnum;
import co.odin.transformers.ContentRequestTransformer;
import co.odin.transformers.HeadersRequestTransformer;
import co.odin.transformers.URLRequestTransformer;
import co.odin.utils.HermesMachine;


public class HermesMachine {
	private static MessageSource mrc = MessageSourceConfig.messageSource();
	private static HermesMachine instance;
	private static final Logger logger = LogManager.getLogger(HermesMachine.class);
	private static List<Menu> permisosPerfilparaPerfil = new ArrayList<Menu>();

	private static String rpassEmailHTMLTemplateString;
	private static final String emailFileName = "email/resetPasswordEmailHTMLTemplate.html";
	private static Properties props = new Properties();
	private static boolean configured = false;
	private ConfiguracionAurora seeConfig;
	private static MailConfiguration mailconf;
	private static Environment springEnvironment;

	private HermesMachine() {
	}

	public static HermesMachine instance() {
		if (instance == null)
			instance = new HermesMachine();
		return instance;
	}

	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * 
	 * @param depth
	 *            depth in the call stack (0 means current method, 1 means call
	 *            method, ...)
	 * @return method name
	 */
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		return ste[ste.length - 1 - depth].getMethodName(); 
															
	}

	public static boolean isThereNull(String... vars) {
		for (String var : vars)
			if (var == null)
				return true;
		return false;
	}

	/**
	 * Consume un servicio web RESTful
	 * 
	 * @param headerContentType
	 * @param serviceUrl
	 * @param method
	 * @param responseType
	 * @param httpEntitty
	 * @return
	 */
	public static <T> ResponseEntity<?> consumeRestService(MediaType headerContentType, String serviceUrl,
			HttpMethod method, Class<T> responseType, Object httpEntitty) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(headerContentType);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> httpEntity = new HttpEntity<>(httpEntitty, headers);
		return restTemplate.exchange(serviceUrl, method, httpEntity, responseType);
	}

	private String getSeeConfigServiceUrl(String serviceResource) {
		StringBuilder sb = new StringBuilder();
		sb.append(springEnvironment.getProperty("heraclesservice.protocol"));
		sb.append(springEnvironment.getProperty("heraclesservice.host"));
		sb.append(springEnvironment.getProperty("heraclesservice.port"));
		sb.append(springEnvironment.getProperty("heraclesservice.context"));
		switch (serviceResource.toLowerCase()) {
		case "seeconfig":
			sb.append(springEnvironment.getProperty("heraclesservice.seeConfResource"));
			break;
		case "appconf":
			sb.append(springEnvironment.getProperty("heraclesservice.appConfResource"));
			break;
		}
		return sb.toString();
	}

	public static String getServiceURL(String serviceName, String serviceResource, Environment springEnvironment,
			String... getParameters) {

		switch (serviceName) {
		case "pdservice": {
			StringBuilder sb = new StringBuilder();
			sb.append(springEnvironment.getProperty("pdservice.protocol"));
			sb.append(springEnvironment.getProperty("pdservice.host"));
			sb.append(springEnvironment.getProperty("pdservice.port"));
			sb.append(springEnvironment.getProperty("pdservice.context"));
			switch (serviceResource) {
			case "diccionarioresource":
				sb.append(springEnvironment.getProperty("pdservice.diccionarioresource"));
				sb.append("?accesstoken=").append(getParameters);
				break;
			}
			return sb.toString();
		}
		case "prservice": {
			StringBuilder sb = new StringBuilder();
			sb.append(springEnvironment.getProperty("prservice.protocol"));
			sb.append(springEnvironment.getProperty("prservice.host"));
			sb.append(springEnvironment.getProperty("prservice.port"));
			sb.append(springEnvironment.getProperty("prservice.context"));
			switch (serviceResource) {
			case "getPasswordReset":
				sb.append(springEnvironment.getProperty("prservice.getPasswordResetResource"));
				sb.append("?").append("login=").append(getParameters[0]);
				break;
			case "resetPassword":
				sb.append(springEnvironment.getProperty("prservice.resetPasswordResource"));
				sb.append("?").append("login=").append(getParameters[0]).append("&").append("email=")
						.append(getParameters[1]);
				break;
			case "deletePasswordReset":
				sb.append(springEnvironment.getProperty("prservice.deletePasswordResource")).append("/")
						.append(getParameters[0]);
				break;
			}
			return sb.toString();
		}
		case "tokenservice": {
			StringBuilder sb = new StringBuilder();
			switch (serviceResource.toLowerCase()) {
			case "gettoken":
				sb.append(springEnvironment.getProperty("authservice.gettokenserviceurl"));
				sb.append(springEnvironment.getProperty("tokenservice.clientid")).append("/");
				sb.append(springEnvironment.getProperty("tokenservice.inum")).append("/");
				sb.append(getParameters[0]);
				return sb.toString();
			case "newuser":
				sb.append(springEnvironment.getProperty("authservice.newuserserviceurl"));
				sb.append(springEnvironment.getProperty("tokenservice.clientid")).append("/");
				sb.append(springEnvironment.getProperty("tokenservice.inum")).append("/");
				sb.append(getParameters[0]);
				return sb.toString();
			case "deleteuser":
				sb.append(springEnvironment.getProperty("authservice.eraseusererviceurl"));
				sb.append(springEnvironment.getProperty("tokenservice.clientid")).append("/");
				sb.append(springEnvironment.getProperty("tokenservice.inum")).append("/");
				sb.append(getParameters[0]);
				return sb.toString();
			}

		}
		case "configService":
			StringBuilder sb = new StringBuilder();
			sb.append(springEnvironment.getProperty("heraclesservice.protocol"));
			sb.append(springEnvironment.getProperty("heraclesservice.host"));
			sb.append(springEnvironment.getProperty("heraclesservice.port"));
			sb.append(springEnvironment.getProperty("heraclesservice.context"));
			switch (serviceResource.toLowerCase()) {
			case "seeconfig":
				sb.append(springEnvironment.getProperty("heraclesservice.seeConfResource"));
				break;
			case "appconf":
				sb.append(springEnvironment.getProperty("heraclesservice.appConfResource"));
				break;
			}
			return sb.toString();
		default:
			return "";
		}
	}

	public static MenuTemplate construirMenuGeneral(List<Menu> opcionesMenu) {
		MenuTemplate rootNode = new MenuTemplate();
		List<MenuTemplate> parentsNodes = new ArrayList<MenuTemplate>();

		for (Menu menu : opcionesMenu) {
			if (menu.getNivel() != null && menu.getNivel() == AppConstants.General.MENU_NIVEL_ROOT) {
				rootNode.setLabel(menu.getEtiqueta());
			}
			if (menu.getNivel() != null && menu.getNivel() == AppConstants.General.MENU_NIVEL_UNO
					&& menu.getTipoMenu().equalsIgnoreCase(TipoMenuEnum.GENERAL.name())) {
				MenuTemplate parentNode = asignarValoresMenuToMenuTemplate(menu, opcionesMenu, null);
				parentNode.setChildren(seekChildren(opcionesMenu, menu.getId()));
				parentsNodes.add(parentNode);
			}

			if (menu.getNivel() == AppConstants.General.MENU_NIVEL_TRES
					&& menu.getMenuPadre().getEtiqueta().toLowerCase().contains(AppConstants.General.CADENA_PERFIL)) {
				permisosPerfilparaPerfil.add(menu);
			}
		}
		parentsNodes = ordenarListaMenuTemplate(parentsNodes);
		rootNode.setChildren(parentsNodes);
		return rootNode;
	}

	public static MenuTemplate asignarValoresMenuToMenuTemplate(Menu menu, List<Menu> menusPerfil,
			Integer grandParentId) {

		MenuTemplate menuTemplate = new MenuTemplate();
		menuTemplate.setLabel(menu.getEtiqueta());
		menuTemplate.setId(menu.getId());

		// Establece el idGrandParent
		menuTemplate.setIdGrandParent(grandParentId);

		menuTemplate.setIdPadre(menu.getMenuPadre().getId());
		menuTemplate.setOrden(menu.getOrden());
		menuTemplate.setTipoMenu(menu.getTipoMenu());
		menuTemplate.setIsSelected(AppConstants.General.ESTADO_DESHABILITADO);

		/*
		 * Si viene vacia la lista de menusPerfil, quiero que se arme el
		 * MenuTemplate con TODAS las opciones de Menú, sin hacer una búsqueda
		 * en los menusPerfil
		 */
		if (menusPerfil != null && menusPerfil.size() != 0) {
			if (menusPerfil.stream().anyMatch(m -> m.getId().equals(menu.getId())))
				menuTemplate.setIsSelected(AppConstants.General.ESTADO_HABILITADO);
		}

		menuTemplate.setResource((menu.getUriServicio()));
		return menuTemplate;
	}

	private static List<MenuTemplate> ordenarListaMenuTemplate(List<MenuTemplate> list) {
		Collections.sort(list, new Comparator<MenuTemplate>() {
			@Override
			public int compare(MenuTemplate mT1, MenuTemplate mT2) {
				return mT1.getOrden().compareTo(mT2.getOrden());
			}
		});
		return list;
	}

	public static List<MenuTemplate> seekChildren(List<Menu> opcionesMenu, Integer parentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		for (Menu opcionMenu : opcionesMenu) {
			if (opcionMenu.getMenuPadre() != null && opcionMenu.getMenuPadre().getId().compareTo(parentId) == 0
					&& opcionMenu.getNivel() == AppConstants.General.MENU_NIVEL_DOS) {
				MenuTemplate child = new MenuTemplate();
				child.setLabel(opcionMenu.getEtiqueta());
				child.setId(opcionMenu.getId());
				child.setIdPadre(opcionMenu.getMenuPadre().getId());
				child.setOrden(opcionMenu.getOrden());
				child.setIsSelected(AppConstants.General.ESTADO_HABILITADO);
				child.setResource((opcionMenu.getUriServicio()));
				children.add(child);
			}
		}
		children = ordenarListaMenuTemplate(children);
		return children;
	}

	public static List<MenuTemplate> seekChildrenPerfilToUpdate(List<Menu> todosMenus, List<Menu> menusPerfil,
			Integer parentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		// Recorre todas las opciones de Menú
		for (Menu menu : todosMenus) {

			// Filro por nivel de profundidad 2 (Hijos) Ó si se trata de un
			// menú de botones (todo de Parametrización o Gestión de Demandas)
			if (menu.getNivel() != null && menu.getNivel().compareTo(AppConstants.General.MENU_NIVEL_DOS) == 0
					|| (menu.getTipoMenu().equalsIgnoreCase(TipoMenuEnum.ESPECIFICO.name()))) {

				if (menu.getMenuPadre() != null && menu.getMenuPadre().getId().compareTo(parentId) == 0) {

					MenuTemplate child = asignarValoresMenuToMenuTemplate(menu, menusPerfil, null);
					children.add(child);

					/*
					 * Busca los nietos (Nivel 3), le pasa el parentId que
					 * corresponde al grandParentId
					 */

					child.setChildren(seekNietos(todosMenus, menusPerfil, menu.getId(), parentId));
				}
			}
		}
		children = ordenarListaMenuTemplate(children);
		return children;
	}

	public static List<MenuTemplate> seekNietos(List<Menu> todosMenus, List<Menu> menusPerfil, Integer parentId,
			Integer grandParentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		// Recorre todas las opciones de Menú
		for (Menu menu : todosMenus) {

			// Filro por nivel de profundidad 3 (Nietos)
			if (menu.getNivel() != null && menu.getNivel().compareTo(AppConstants.General.MENU_NIVEL_TRES) == 0) {

				// Adiciono únicamente a los hijos del parámetro id Padre
				if (menu.getMenuPadre().getId() != null && menu.getMenuPadre().getId().compareTo(parentId) == 0) {
					MenuTemplate child = asignarValoresMenuToMenuTemplate(menu, menusPerfil, grandParentId);
					children.add(child);
				}
			}
		}
		return children;
	}

	public static List<MenuTemplate> construirPermisosPerfilParaPerfil(List<Menu> todasOpcionesMenu,
			List<Menu> opcionesMenuPerfil) {
		List<Menu> todosPermisosPeriles = new ArrayList<Menu>();
		List<MenuTemplate> menuTemplatePermisos = new ArrayList<MenuTemplate>();

		// Obtengo únicamente TODOS los permisos CRUD para perfiles
		for (Menu menu : todasOpcionesMenu) {
			if (menu.getNivel() == AppConstants.General.MENU_NIVEL_TRES
					&& menu.getMenuPadre().getEtiqueta().toLowerCase().contains(AppConstants.General.CADENA_PERFIL)) {
				todosPermisosPeriles.add(menu);
			}
		}

		// primero ordeno para armar el menuTemplate con el CRUD que necesita
		// FRONT
		todosPermisosPeriles = ordenarListaMenu(todosPermisosPeriles);

		// Se recorre la lista de TODOS los permisos CRUD
		for (Menu menu : todosPermisosPeriles) {

			MenuTemplate mtPermiso = asignarValoresMenuToMenuTemplate(menu, opcionesMenuPerfil, null);
			menuTemplatePermisos.add(mtPermiso);
		}
		return menuTemplatePermisos;
	}

	public static List<Menu> ordenarListaMenu(List<Menu> list) {
		Collections.sort(list, new Comparator<Menu>() {
			@Override
			public int compare(Menu menu1, Menu menu2) {
				return menu1.getOrden().compareTo(menu2.getOrden());
			}
		});
		return list;
	}

	public static MenuTemplate construirMenuToUpdateOrCreatePerfil(List<Menu> todosMenus, List<Menu> menusPerfil) {
		MenuTemplate rootNode = new MenuTemplate();
		List<MenuTemplate> parentsNodes = new ArrayList<MenuTemplate>();
		for (Menu menu : todosMenus) {
			if (menu.getNivel() != null && menu.getNivel() == AppConstants.General.MENU_NIVEL_ROOT) {
				rootNode.setLabel(menu.getEtiqueta());
			}
			if (menu.getNivel() != null && menu.getNivel() == AppConstants.General.MENU_NIVEL_UNO) {
				MenuTemplate parentNode = asignarValoresMenuToMenuTemplate(menu, menusPerfil, null);
				parentNode.setChildren(seekChildrenPerfilToUpdate(todosMenus, menusPerfil, menu.getId()));
				parentsNodes.add(parentNode);
			}
		}
		parentsNodes = ordenarListaMenuTemplate(parentsNodes);
		rootNode.setChildren(parentsNodes);
		return rootNode;
	}

	public static String convertToJson(Object dataToJsonize) {
		try {
			return dataToJsonize != null ? new Gson().toJson(dataToJsonize) : "null";
		} catch (Exception e) {
			String errorMessage = "Excepción de Gson\n".concat(e.getLocalizedMessage());
			Response errorResponse = new Response();
			errorResponse.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			errorResponse.setMessage(errorMessage);
			logger.error(errorMessage);
			errorMessage = null;
			return new Gson().toJson(errorResponse);
		}
	}

	/**
	 * Consume el servicio de diccionario de contraseñas y resuelve si la
	 * palabra está en la lista de palabras restringidas.
	 * 
	 * @param clave
	 * @param accessToken
	 * @param springEnvironment
	 * @return
	 */
	public static boolean inRestrictedWords(String clave, String accessToken, Environment springEnvironment) {
		String serviceUrl = getServiceURL("pdservice", "diccionarioresource", springEnvironment, accessToken);
		ResourceResponse response = (ResourceResponse) consumeRestService(MediaType.APPLICATION_JSON, serviceUrl,
				HttpMethod.GET, ResourceResponse.class, null).getBody();

		String stringComplete = response.getOtrasOpciones().getDiccionario();

		if (stringComplete.trim().length() > 0) {
			String[] words = stringComplete.split(";");
			for (int i = 0; i < words.length; i++) {
				if (clave.toLowerCase().contains(words[i].toLowerCase()))
					return true;
			}
		}
		return false;
	}

	/****************************************************************
	 * Configuracion Password Reset Login
	 */

	public RResponse createEmail(String userLogin, String email) {

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailconf.getMail_from(), mailconf.getMail_pass());
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(mailconf.getMail_from());
			helper.setTo(email);
			helper.setSubject("Reinicialización de contraseña | BVC - Sistema de Emisiones y Enajenaciones");

			RResponse resetResponse = createResetEmailTextBody(userLogin, email);
			helper.setText(resetResponse.getMessage(), true); // Mensaje HTML

			String res = "Correo electrónico listo para enviar a usuario " + userLogin;
			logger.info(res);
			resetResponse.setEmailMessage(message);
			return resetResponse;
		} catch (MessagingException e) {
			String res = "Excepción enviando correo electrónico de reinicialización de contraseña para usuario "
					.concat(userLogin).concat("\n" + e.getLocalizedMessage());
			logger.error(res);
			e.printStackTrace();
			return null;
		}
	}

	public static void getMailConfig(Environment springEnv, MailConfiguration mailconf2) {
		if (!configured) {
			setMailConf(mailconf2);
			setSpringEnv(springEnv);
			System.out.println("springEnvironment: " + springEnvironment);
			System.out.println("mailconf2: " + mailconf2);
			props.put("mail.smtp.auth", mailconf.getMail_smtp_auth());
			props.put("mail.smtp.starttls.enable", mailconf.getMail_smtp_starttls_enable());
			props.put("mail.smtp.host", mailconf.getMail_smtp_host());
			props.put("mail.smtp.port", mailconf.getMail_smtp_port());

			ClassPathResource classPathResource = new ClassPathResource(emailFileName);

			try {
				byte[] bdata = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
				rpassEmailHTMLTemplateString = new String(bdata, StandardCharsets.UTF_8);
				configured = true;
			} catch (IOException e) {
				String res = "Excepción leyendo el contenido de la plantilla de reinicialización"
						+ " de contraseña".concat("\n" + e.getLocalizedMessage());
				logger.error(res);
				e.printStackTrace();
			}
		}
	}

	private static void setSpringEnv(Environment springEnv) {
		springEnvironment = springEnv;
	}

	private static void setMailConf(MailConfiguration mailconf2) {
		mailconf = mailconf2;
	}

	private RResponse createResetEmailTextBody(String userLogin, String email) {
		RResponse res = new RResponse();

		/** Consumir el SeeConfigService **/
		String seeConfigServiceUrl = getSeeConfigServiceUrl("seeConfig");
		logger.info("about to consume seeConfig service at " + seeConfigServiceUrl);
		ResponseEntity<?> seeConfigResponse = consumeRestService(MediaType.TEXT_PLAIN, seeConfigServiceUrl,
				HttpMethod.GET, ConfiguracionAurora.class, null);
		seeConfig = (ConfiguracionAurora) seeConfigResponse.getBody();
		logger.info("see config: " + seeConfig);
		/**
		 * Guardar en BD un hash único para esta solicitud, para que el método
		 * de reinicializar contraseña lo encuentre
		 **/
		// String resetRequestUniqueID = getHashID(userLogin, request);
		String otp = getOTP();

		String message = rpassEmailHTMLTemplateString.replace("{{nombreUsuario}}", userLogin);
		message = message.replace("{{default_pass_time}}",
				String.valueOf(seeConfig.getTiempo_expiracion_clave_temporal()));
		message = message.replace("{{plain_password}}", otp);
		message = message.replace("{{year}}", Calendar.getInstance().get(Calendar.YEAR) + "  ");

		res.setCode(33);
		res.setYear(Calendar.getInstance().get(Calendar.YEAR) + "");
		res.setMessage(message);
		res.setTemporalPassword(otp);
		return res;
	}

	private String getOTP() {
		return new String(generateOTP(OTP_DIGIT_NUMBER));
	}

	private char[] generateOTP(int len) {
		String values = CAPITAL_CHARS + SMALL_CHARS + NUMBERS + SYMBOLS;
		Random rndm_method = new Random();

		char[] password = new char[len];

		for (int i = 0; i < len; i++)
			password[i] = values.charAt(rndm_method.nextInt(values.length()));

		return password;
	}

	public ConfiguracionAurora getSeeConfig() {
		return seeConfig;
	}

	public static Integer getUserIdInSession(String accessToken, Environment springEnv, Logger logger) {
		String serviceUrl = getServiceURL("cerbero", "getUserInSession", springEnv, accessToken);
		Response res = null;
		ResponseEntity<?> rent = null;
		try {
			rent = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrl, HttpMethod.GET,
					Response.class, null);
			res = (Response) rent.getBody();
			logger.info("Cerbero response: " + rent);
			if (rent.getStatusCode().equals(HttpStatus.SC_OK))
				return Integer.valueOf(res.getResource());
		} catch (Exception e) {
			logger.error("Excepción averiguando si existe sesión en Cerbero:\n".concat(e.getMessage()));
		}
		return -1;
	}

	public static <T> ResponseEntity<?> consumeRestService(MediaType headerContentType, String serviceUrl,
			HttpMethod method, ParameterizedTypeReference<List<ServiceDTO>> parameterizedTypeReference,
			Object httpEntitty) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(headerContentType);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> httpEntity = new HttpEntity<>(httpEntitty, headers);
		return restTemplate.exchange(serviceUrl, method, httpEntity, parameterizedTypeReference);
	}
	public static String getIPFromRequest(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		
		if (null == ip || ip.length() == 0)
			ip = request.getRemoteAddr();

		logger.info(mrc.getMessage("odin.security.autenticacion.request.IP.info", null, null) + ip);
		return ip;
	}
	
	public static String convertToJsonGW(Object dataToJsonize) {
		try {
			return dataToJsonize != null ? new Gson().toJson(dataToJsonize) : "null"; 			
		}
		catch (Exception e) {
			String errorMessage = "Excepción de Gson\n".concat(e.getLocalizedMessage());
			Response errorResponse = new Response();
			errorResponse.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			errorResponse.setMessage(errorMessage);
			logger.error(errorMessage);
			errorMessage = null;
			return new Gson().toJson(errorResponse);
		}	
	}
	
	public static HttpUriRequest createHttpRequest(HttpServletRequest request, String serviceUrl, MultipartFile file) throws URISyntaxException, NoSuchRequestHandlingMethodException, IOException {
	    URLRequestTransformer urlRequestTransformer = new URLRequestTransformer();
	    urlRequestTransformer.setServiceUrl(serviceUrl);
	    ContentRequestTransformer contentRequestTransformer = new ContentRequestTransformer();
	    HeadersRequestTransformer headersRequestTransformer = new HeadersRequestTransformer();
	    headersRequestTransformer.setPredecessor(contentRequestTransformer);
	    contentRequestTransformer.setPredecessor(urlRequestTransformer);
	    return headersRequestTransformer.transform(request, file).build();
	 }
	 
	 public static String read(InputStream input) throws IOException {
		    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input,"UTF-8"))) {
		      return buffer.lines().collect(Collectors.joining("\n"));
		    }
	 }
	 
	 public static HttpHeaders makeResponseHeaders(HttpResponse response) {
		    HttpHeaders result = new HttpHeaders();
		    Header h = response.getFirstHeader("Content-Type");
		    if(h != null)
		    	result.set(h.getName(), h.getValue());
		    return result;
	 }

}