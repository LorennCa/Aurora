package co.ias.usuarios.utils;

import java.util.Calendar;


/**
 * Clase que contiene los valores constantes de la aplicación
 * 
 * @author Diego_Babativa
 * @version 1.0
 * @since 23/05/2017
 *
 */
public interface AppConstants {
	public static final String IAS_INITIAL_CONFIGURATION_CONTEXT = "/appConf";
	public static final String DEFAULT_PROTOCOL = "http://";
	public static final String UPDATE_USER_PASSWORD_CONTEXT = "/cambio-clave";
	public static final String PASSWORD_HISTORY_CONTEXT = "/cambio-clave-historico";
	public static final String DICTIONARY_PASSWORD = "/cambio-clave-diccionario";
	public static final String PASSWORD_DICTIONARY_UPDATE_CONTEXT = DICTIONARY_PASSWORD;
	public static final String SLASH = "/";
	public static final String TOKEN_CONTEXT = "/token";
	public static final String PASSWORD_HISTORY_CONTEXT_PATH = PASSWORD_HISTORY_CONTEXT;
	public static final String VALIDATE_PROFILE_FOR_DELETE_CONTEXT = "/validar-perfil-eliminar/";
	public static final String VALIDATE_USER_ENTITY_IDENTIFICATION_CONTEXT = "/validar-usuario-entidad";
	public static final String IAS_GENERAL_ACCESS_VALIDATE_DATE_CONTEXT = "/validar-fecha-hora";
	public static final String USUARIOS_VALIDAR_PERFILES_HIJOS = "/validar-perfiles-hijos";
	public static final String PASSWORD_DICTIONARY_CONTEXT = "/usuarios/diccionario";
	public static final String AUTHENTICATION_CONTEXT = "/authenticate";
	public static final String FILE_SERVICE_NAME_CONST = "fileservice";
	public static final String RESET_USER_PASSWORD_CONTEXT = "/resetPassword";
	
	
	public static final String GATEWAY_PATH = "/IAS/usuarios";
	public static final String API_VERSION = "/v1";
	
	public static final String GATEWAY_ROOT_CONTEXT = GATEWAY_PATH + API_VERSION;
	
	
	
	
	
	
	
	public static final String AUTHORIZATION_SCOPE = "scope";
	public static final String AUTHORIZATION_ACCESS_TOKEN = "accesstoken";
	public static final String AUTHORIZATION_URI = "uri";
	public static final String ACCESS_TOKEN_CONST= "accesstoken";
	public static final String KERBEROS_LOGIN_PATH_GW = "/IAS/usuarios/v1/authenticate/login";
	public static final String SERVICE_ROOT_CONTEXT = "/usuarios/v1";
	public static final String AUTHENTICATION_PATH = "/authenticate";
	public static final String GENERAL_ACCESS_CONTEXT = "/acceso-general";
	public static final String AUTH_LOGIN_PATH = AUTHENTICATION_PATH + "/login";
	public static final String AUTH_LOGOUT_PATH = AUTHENTICATION_PATH + "/logout";
	public static final String KERBEROS_AUTHENTICATION_PATH = SERVICE_ROOT_CONTEXT + AUTHENTICATION_PATH;
	public static final String KERBEROS_LOGIN_PATH = KERBEROS_AUTHENTICATION_PATH + "/login";
	public static final String KERBEROS_LOGOUT_PATH = KERBEROS_AUTHENTICATION_PATH + "/logout";
	public static final String KERBEROS_GET_USER_INSESSION_CONTEXT = "/getUserInSession";
	public static final String NO_USER_IN_SESSION_CONSTANT = "<no-user-in-session>";
	public static final String UNIX_LOGO_PATH_SUFFIX ="logo/";
	public static final String IAS_SECUTIRY_APP_SWAGGER_PATH = "/swagger-ui.html";
	public static final String LETO_PATH = "/IAS/usuarios/v1/leto/**";
	public static final String IAS_RESET_PASS_PATH = "/IAS/usuarios/v1/usuarios/resetPassword";
	public static final String UNIX_REPORTS_PATH_SUFFIX ="reports/";
	public static final String WIN_LOGO_PATH_SUFFIX = "logo\\";
	public static final String WIN_REPORTS_PATH_SUFFIX = "reports";
	public static final String FILE_UPLOAD_PATH = "/uploadFile";
	public static final String FILE_UPLOAD_REST_API = SERVICE_ROOT_CONTEXT + FILE_UPLOAD_PATH;
	public static final String THIS_SERVICE_NAME = "Usuarios";
	public static interface General {

		// Gestión de usuarios
		public static final String BLOQUEO_IP_PERMITIDA = "BLOQUEO_IP";
		public static final String BLOQUEO_INTENTOS_PERMITIDOS = "BLOQUEO_INTENTOS";
		public static final Integer ID_DICCIONARIO_CLAVES = 1;
		public static final Integer STRENGTH_PASSWORD_ENCODER = 11;
		public static final Integer AUTO_INCREMENT_LOGIN = 1;

		// Generación claves codificadas:
		// Salt - minimo de 16 bytes!!!
		public static final String SALT = "1234567890123456";

		// Niveles de arbol de menú
		public static final Integer MENU_NIVEL_ROOT = 0;
		public static final Integer MENU_NIVEL_UNO = 1;
		public static final Integer MENU_NIVEL_DOS = 2;
		public static final Integer MENU_NIVEL_TRES = 3;

		
		public static final String TIPO_MENU_CONSULTA = "CONSULTA";
		
		
		// Constantes para la administración de Perfiles
		public static final Integer PERFIL_SUPER_ADMINISTRADOR_ID = 1;

		// Constantes para la administración de Usuarios
		public static final Integer USUARIO_SUPER_ADMINISTRADOR_ID = 1;
		
		public static final Integer TIPO_DOCUMENTO_NIT = 2;

		// Estados y validación de recursos
		public static final boolean ESTADO_HABILITADO = Boolean.TRUE;
		public static final boolean ESTADO_DESHABILITADO = Boolean.FALSE;
		public static final String AUTORIZACION_IP = "ip";
		public static final String AUTORIZACION_SCOPE = "scope";
		public static final String ACCESS_TOKEN_VALUE = "accesstoken";
		public static final String AUTORIZACION_URI = "uri";

		// Regex patterns:
		public static final CharSequence CADENA_PERFIL = "perf";
		public static final String CADENA_USUARIO = "usua";
		public static final String CADENA_DEMANDA = "dema";
		public static final String HOUR_PATTERN = "hh:mm:ss";
		public static final String DATE_PATTERN = "dd-MM-yyyy";
		public static final String DATE_HOLIDAY_PATTERN = "yyyy-MM-dd";
		public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
		public static final String DECIMAL_FORMAT_PATTERN = "###,###.####";
	
		/*
		 * Valdiaciones de negocio (Respuestas constraints de base de datos o
		 * servidor Gluu)
		 */
		public static final String VALIDATE_NOMBRE_UNIQUE = "nombre_UNIQUE";
		public static final String VALIDATE_LOGIN_UNIQUE = "Duplicate UID value";
		public static final String VALIDATE_LOGIN_UNIQUE_MYSQL = "login_UNIQUE";
		public static final String VALIDATE_IAS_GLUU_CONNECTION_UNAUTHORIZED = "HTTP 401 (Unauthorized) status code";

		public static final String IAS_APPLICATION_CONFIGURATION = "IAS_APP_CONFIGURATION";

		public static final String OS_WINDOWS_NAME = "Windows";
		public static final String OS_UNIX_NAME = "Unix";

	}


	public static final int DYNAMIC_LINK_EXPIRE_TIME = 30;
	public static final int DYNAMIC_LINK_EXPIRE_FORMAT = Calendar.MINUTE;
	public static final String TEST_ENVIRONMENT = "TEST";
	
	/** Insumos para generación de contraseñas temporales **/
	public static final String USER_IN_SESSION_RESOURCE_PATH = SERVICE_ROOT_CONTEXT + "/getUserInSession";
	
	/**Codigo de error para Usuarios reset**/
	public static final Integer IAS_RESET_NOT_FOUND = 100;
	public static final Integer IAS_RESET_NOT_AUTHORIZED = 101;
	
	/*Codigos de negocio de Usuarios*/
	public static final Integer IAS_USER_ROL_ENTIDAD_FOUND = 101;
	public static final Integer IAS_USER_ROL_ENTIDAD_OK = 100;
	public static final Integer IAS_USER_ROL_ENTIDAD_ONE = 102;
	public static final Integer IAS_USER_ROL_ENTIDAD_TWO = 103;
	
	/*titulos repotes*/
	public static String TITULO_REPORTE_PERFILES = "Reporte de Perfiles existentes Sistema";
	public static String TITULO_REPORTE_USUARIOS = "Reporte de Usuarios existentes Sistema"; 

}