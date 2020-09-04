package co.ias.usuarios.utils;

import java.util.Calendar;

public class AppPaths {

	// Rutas de IAS
	public static final String REST_API_VERSION_1 = "/v1";
	public static final String IAS_REST_API_PATH = "/IAS";
	public static final String CERBERO_ROOT_CONTEXT = "/usuarios";
	public static final String IAS_ROOT_CONTEXT = IAS_REST_API_PATH + REST_API_VERSION_1;
	public static final String SERVICE_ROOT_CONTEXT = "/usuarios/v1";

	// Acceso genérico a recurso por Id
	public static final String ID_PARAM_VALUE = "/{id}";
	public static final String PATH_VARIABLE_ID = "id";

	// Rutas de acceso para cualquier usuario:
	public static final String IAS_GENERAL = "/acceso-general";
	public static final String IAS_GENERAL_ACCESS = IAS_ROOT_CONTEXT + IAS_GENERAL;
	public static final String IAS_GENERAL_ACCESS_VALIDATE_PASSWORD_HISTORICO = "/cambio-clave-historico";
	public static final String IAS_GENERAL_ACCESS_UPDATE_PASSWORD = "/cambio-clave";
	public static final String USER_PASSWORD_UPDATE_PASSWORD_CONTEXT = IAS_GENERAL + "/cambio-clave" + ID_PARAM_VALUE;
	public static final String IAS_GENERAL_ACCESS_VALIDATE_PASSWORD_DICCIONARIO = IAS_GENERAL + "/cambio-clave-diccionario";
	public static final String GENERAL_ACCESS_VALIDATE_USER_ENTITY_ID_CONTEXT = "/validar-usuario-entidad";
	public static final String VALIDATE_PROFILE_FOR_DELETE_CONTEXT = "/validar-perfil-eliminar";
	public static final String IAS_GENERAL_ACCESS_VALIDATE_TOKEN = IAS_GENERAL + "/token";
	/* Nuevo path para consultar fecha y hora del servidor*/
	public static final String IAS_GENERAL_ACCESS_VALIDATE_DATE = "/validar-fecha-hora";
	public static final String IAS_APP_CONF_PATH = "/appConf";
	public static final String IAS_APP_CONF_PATH_GW = "/IAS/usuarios/v1/acceso-general/appConf";
	public static final String IAS_APP_CONF_ABSOLUTE_PATH = IAS_GENERAL_ACCESS + IAS_APP_CONF_PATH;
	public static final String PASSWORD_HISTORY_CONTEXT_A = "/acceso-general/cambio-clave-historico";
	public static final String ID_SIMPLE_VARIABLE_PATH_CONTEXT = "/{id}";

	// ****************************************************************************
		// constantes de las URIs de acceso a los recursos de Usuarios
		public static class Usuarios {
			public static final String PATH = "/usuarios";
			public static final String IAS_USUARIOS = IAS_ROOT_CONTEXT + PATH; 
			public static final String IAS_USUARIOS_VALIDATE_PASSWORD = "/cambio-clave";
			public static final String RESET_PASS_PATH = "/resetPassword";
			public static final String RESET_PASS_PATH_2 = "/resetPassword";
			public static final String SEND_PASS_RESET_EMAIL = "/sendPassResetEmail";
			public static final String USUARIOS_VALIDAR_PERFILES_HIJOS = "/validar-perfiles-hijos";

		}

	// ****************************************************************************
	// Constantes de las URIs de acceso a los recursos de autenticación:
	public static class Security {
		public static final String IAS_SECURITY_AUTHENTICATE_PATH = IAS_ROOT_CONTEXT
				+ "/authenticate";
		public static final String IAS_SECURITY_LOGIN_PATH = "/login";
		public static final String IAS_SECURITY_LOGOUT_PATH = "/logout";
		public static final String IAS_SECURITY_APP_LOGIN_ABSOLUTE_PATH = IAS_SECURITY_AUTHENTICATE_PATH
				+ IAS_SECURITY_LOGIN_PATH;
		public static final String IAS_SECUTIRY_APP_SWAGGER_PATH = "/swagger-ui.html";
		public static final String IAS_SEND_RESET_MAIL_PATH = IAS_REST_API_PATH + REST_API_VERSION_1 + Usuarios.PATH 
				                   + Usuarios.SEND_PASS_RESET_EMAIL;
		public static final String RESET_PASS_CONTEXT = CERBERO_ROOT_CONTEXT + REST_API_VERSION_1 + Usuarios.RESET_PASS_PATH;
	}

	

	// ****************************************************************************
	// constantes de las URIs de acceso a los recursos de Perfiles
	public static class Perfil {
		public static final String PATH = "/perfiles";
		public static final String IAS_PERFILES = IAS_ROOT_CONTEXT + PATH;
	}

	// *******************************************************************************
	// Constantes de las URIs de acceso a los recursos de Diccionario de claves
	public static class DiccionarioClaves {
		public static final String PATH = "/diccionario";
		public static final String IAS_DICCIONARIO_CLAVES = Usuarios.IAS_USUARIOS
				+ PATH;
	}

	
	public static class Dictionary{
		public static final String THIS_SERVICE_NAME_DICTIONARY = "PDService";
		public static final String SERVICE_ROOT_CONTEXT_DICTIONARY = "/passwordDictionary";
		

		public static final String IAS_USER_PASSWORD_CONTEXT = "/usuario/v1";
		public static final String PATH = "/diccionario";
		public static final String IAS_DICCIONARIO_CLAVES = "/usuarios" + PATH;
		public static final String IAS_USER_PASSWORD_COMPLETE_CONTEXT = IAS_USER_PASSWORD_CONTEXT +
																			IAS_DICCIONARIO_CLAVES;

		public static final String SERVICE_GENERAL_PATH = SERVICE_ROOT_CONTEXT + Dictionary.IAS_DICCIONARIO_CLAVES;
		public static final String PASSWORD_DICTIONARY_CONTEXT = "/cambio-clave-diccionario";
		public static final String IN_RESTRICTED_WORDS_CONTEXT = "/inRestrictedWords";
		
	}
	
	public static class PasswordReset{
		public static final String THIS_SERVICE_NAME = "PasswordResetService";
		public static final String SERVICE_ROOT_CONTEXT_PASSRESET = "/passwordResetService";	
		public static final String RESET_PASS_PATH = "/resetPassword";
		
		/** Insumos para generación de contraseñas temporales **/
		public final static Integer OTP_DIGIT_NUMBER = 8;
		public final static String CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public final static String SMALL_CHARS = "abcdefghijklmnopqrstuvwxyz";
		public final static String NUMBERS = "0123456789";
		public final static String SYMBOLS = "!@#$%^&*_=+-/.?<>)";
		public static final int DYNAMIC_LINK_EXPIRE_FORMAT = Calendar.MINUTE;
		public static final String GET_PASSWORD_RESET_ATTEMPT_PATH = "/getPasswordReset";
		public static final String DELETE_PASSWORD_RESET_ATTEMPT_PATH = "/deletePasswordReset/{login}";
		
	}
	
	public static class Productos{
		public static final String PRODUCTOS = IAS_ROOT_CONTEXT + "/productos";
		public static final String PRODUCTO = "productos";
		public static final String CARRITO = IAS_ROOT_CONTEXT + "/carrito";
		public static final String CARRIT = "carrito";
		public static final String ORDENES = IAS_ROOT_CONTEXT + "/ordenes";
		public static final String ORDEN = "ordenes";
		public static final String PRECOMPRA = IAS_ROOT_CONTEXT + "/precompra";
		public static final String PRECOMP = "precompra";
	}
	
	public static final String ACCESS_TOKEN_VALUE = "accesstoken";
}