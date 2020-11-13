package co.common.utils;

import java.util.Calendar;

public interface CommonAppConstants {
	
	public static final String USER_IN_SESSION_RESOURCE_CONSTANT = "getUserInSession";
	
	// ****************************************************************************
	// Constantes generales de la aplicación
	public static interface General {

		public static final Integer TIPO_DOCUMENTO_NIT = 2;
		public static final String AUTORIZACION_ACCESS_TOKEN = "accesstoken";
		public static final int DYNAMIC_LINK_EXPIRE_TIME = 30;
		public static final int DYNAMIC_LINK_EXPIRE_FORMAT = Calendar.MINUTE;
		public static final String TEST_ENVIRONMENT = "TEST";

		/** Insumos para generación de contraseñas temporales **/
		public final static Integer OTP_DIGIT_NUMBER = 8;
		public final static String CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public final static String SMALL_CHARS = "abcdefghijklmnopqrstuvwxyz";
		public final static String NUMBERS = "0123456789";
		public final static String SYMBOLS = "!@#$%^&*_=+-/.?<>)";
		public static final Integer STRENGTH_PASSWORD_ENCODER = 11;

		public static final String HOUR_PATTERN = "hh:mm:ss";
		public static final String DATE_PATTERN = "dd-MM-yyyy";
		public static final String DATE_HOLIDAY_PATTERN = "yyyy-MM-dd";
		public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
		public static final String DECIMAL_FORMAT_PATTERN = "###,###.####";

		public static final String OS_WINDOWS_NAME = "Windows";
		public static final String OS_UNIX_NAME = "Unix";

	}
}
