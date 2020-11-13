package co.teseo.utils;

public interface AppConstants {
	
	public static final String NO_TOKEN_STRING_CONSTANT = "<no-access-token>";
	// ****************************************************************************
	// Constantes generales de la aplicación
	public interface General {

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

		// Constantes para la administración de Perfiles
		public static final Integer PERFIL_SUPER_ADMINISTRADOR_ID = 1;

		// Constantes para la administración de Usuarios
		public static final Integer USUARIO_SUPER_ADMINISTRADOR_ID = 1;

		// Estados y validación de recursos
		public static final boolean ESTADO_HABILITADO = Boolean.TRUE;
		public static final boolean ESTADO_DESHABILITADO = Boolean.FALSE;
		public static final String AUTORIZACION_IP = "ip";
		public static final String AUTORIZACION_SCOPE = "scope";
		public static final String AUTORIZACION_ACCESS_TOKEN = "accesstoken";
		public static final String AUTORIZACION_URI = "uri";

		// Regex patterns:
		public static final CharSequence CADENA_PERFIL = "perf";
		public static final String CADENA_USUARIO = "usua";
		public static final String CADENA_DEMANDA = "dema";
		public static final String CADENA_OPERACION = "oper";

		// Valdiaciones de negocio (Respuestas constraints de base de datos o
		// servidor Gluu)
		public static final String VALIDATE_NOMBRE_UNIQUE = "nombre_UNIQUE";
		public static final String VALIDATE_LOGIN_UNIQUE = "Duplicate UID value";
		public static final String VALIDATE_LOGIN_UNIQUE_MYSQL = "login_UNIQUE";
		public static final String VALIDATE_SEE_GLUU_CONNECTION_UNAUTHORIZED = "HTTP 401 (Unauthorized) status code";

		public static final String SEE_APPLICATION_CONFIGURATION =  "HERACLES_APP_CONFIGURATION";
	}

}
