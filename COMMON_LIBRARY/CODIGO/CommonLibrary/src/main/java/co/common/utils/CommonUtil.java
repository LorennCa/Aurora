package co.common.utils;

import static co.common.utils.CommonAppConstants.General.CAPITAL_CHARS;
import static co.common.utils.CommonAppConstants.General.NUMBERS;
import static co.common.utils.CommonAppConstants.General.OTP_DIGIT_NUMBER;
import static co.common.utils.CommonAppConstants.General.SMALL_CHARS;
import static co.common.utils.CommonAppConstants.General.SYMBOLS;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.google.gson.Gson;

import co.common.config.MessageSourceConfig;
import co.common.entities.Perfil;


public class CommonUtil {

	
	/**
	 * logger de applicación
	 */
	private static final Logger logger = LogManager.getLogger(CommonUtil.class);

	
	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private static MessageSource mrc = MessageSourceConfig.messageSource();

	@SuppressWarnings("unused")
	private static String getOTP() {		
		return new String(generateOTP(OTP_DIGIT_NUMBER));
	}
	
	private static char[] generateOTP(int len)
    {
        String values = CAPITAL_CHARS + SMALL_CHARS + NUMBERS + SYMBOLS;
        Random rndm_method = new Random();
 
        char[] password = new char[len];
 
        for (int i = 0; i < len; i++)
        	password[i] = values.charAt(rndm_method.nextInt(values.length()));
        
        return password;
    }
	
	@SuppressWarnings("unused")
	private static String getClientOS(HttpServletRequest request) {
		String r = request.getHeader("User-Agent");
		if(r.contains("Windows"))
			return "Windows";
		else if(r.contains("Mac"))
			return "iOS";
		else
			return "Linux";
	}
	
	@SuppressWarnings("unused")
	private static String getBrowser(HttpServletRequest request) {
		logger.info("request header: " + request.getHeader("User-Agent"));
		if(!request.getHeader("User-Agent").contains("Firefox"))
			return "Firefox";
		else if(request.getHeader("User-Agent").contains("OPR"))
			return "Opera";
		else if(request.getHeader("User-Agent").contains("Trident"))
			return "Internet Explorer";
		else
			return "Chrome";
	}


	/**
	 * Encargado de retornar la IP de una petición, dado un Request que entra
	 * como parámetro.
	 * 
	 * @param request
	 * @return
	 */
	public static String getIPFromRequest(HttpServletRequest request) {
		String IP = request.getHeader("X-Forwarded-For");
		if (null == IP || IP.length() == 0) {
			IP = request.getRemoteAddr();
		}
		logger.info(mrc.getMessage(
				"ias.security.autenticacion.request.IP.info", null, null) + IP);

		return IP;
	}

	/**
	 * Utilería encargada de recibir una lista de Strings y ajustarla a una
	 * cadena separada por comas
	 * 
	 * @param listoToconvert
	 * @return
	 */
	public static String ListToString(List<String> listoToconvert) {
		StringBuilder stringBuilder = new StringBuilder();
		if (listoToconvert != null && listoToconvert.size() != 0) {
			for (int i = 0; i < listoToconvert.size(); i++) {
				stringBuilder.append(listoToconvert.get(i));
				if (i != listoToconvert.size() - 1)
					stringBuilder.append(" ");
			}
			return stringBuilder.toString();
		}
		return "";
	}


	/**
	 * Convierte las fechas (Date) y la hora (String) en un formato que entra
	 * como parámetro
	 * 
	 * @param date
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String parseDateTimeToString(Date date, String time,
			String pattern) {

		try {

			SimpleDateFormat format = new SimpleDateFormat(pattern);

			if (date != null) {
				return format.format(date);
			} else {
				if (time != null && time.length() >= 15) {
					time = time.substring(11, 19);
					return time;
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			return "pailas" + e.getMessage();
		}

	}

	/**
	 * Concatena la fecha y hora por separado para retornar un objeto Date
	 * 
	 * @param fecha
	 * @param hour
	 * @return
	 */
	public static Calendar concatDateTime(Date fecha, String hourUnFormatted) {

		String hour = CommonUtil.parseDateTimeToString(null, hourUnFormatted,
				CommonAppConstants.General.HOUR_PATTERN);

		String date = CommonUtil.parseDateTimeToString(fecha, null,
				CommonAppConstants.General.DATE_PATTERN);

		StringBuilder dateTimeBuilder = new StringBuilder();
		dateTimeBuilder.append(date).append(" ").append(hour);

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				CommonAppConstants.General.DATE_TIME_PATTERN);

		try {
			calendar.setTime(sdf.parse(dateTimeBuilder.toString()));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return calendar;
	}

	public static String getOS() {
		String os = System.getProperty("os.name");

		String name = "";

		if (os.startsWith("win") || os.startsWith("Win") || os.startsWith("WIN")) {
			name = CommonAppConstants.General.OS_WINDOWS_NAME;
		} else {
			name = CommonAppConstants.General.OS_UNIX_NAME;
		}
		return name;
	}

	
	/**
	 * Convierte decimales  en un formato que entra
	 * como parámetro
	 * 
	 * @param decimal
	 * @param pattern
	 * @return
	 */
	public static String parseDecimalToString(Double decimal,
			String pattern) {

		try {

			DecimalFormat format = new DecimalFormat(pattern);

			if (decimal != null) {
				return format.format(decimal);
			} else{
				return "";
			}
				
		} catch (Exception e) {
			return "No Convirtio a String" + e.getMessage();
		}

	}

	/**
	 * Convierte a json un conjunto de datos String
	 * 
	 * @param decimal
	 * @param pattern
	 * @return
	 */
	public static String convertToJson(String dato) {

		try {
			
			Gson gson = new Gson();
			
			if(dato != null){
				String json = gson.toJson(dato);				
				return json;
			}else{
				return "";
			}
				
		} catch (Exception e) {
			return "no Convirtio a Json" + e.getMessage();
		}

		
	}
	
	/**
	 * convierte un objeto date en un cadana de acuerdo a un formato que
	 * entra como parametro
	 * 
	 * @param date
	 * @param format
	 * */
	public static String dateToFormat(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static void main(String[] args) {
		
	}
	
	/**
	 * meto-do que convierte una lista de formas pago 
	 * por una lista de tipo string con los nombres de las formas
	 * @param formaPagoList
	 * @return
	 */
//	public static String list(List<TipoFormaPago> formaPagoList) {
//		
//		List<String> slist = new ArrayList<String>();
//		for (TipoFormaPago tipoFormaPago : formaPagoList) {
//			slist.add(tipoFormaPago.getNombre());
//		}
//		
//		return StringUtils.join(slist, ',');
//	}
	
	/**
	 * metodo que devuelve un tipo date a partir del string contenido en un mapa de parametros
	 * @param idParam
	 * @param mapa
	 * @return
	 * @throws Exception
	 */
	public static Date getParamAsDate(Integer idParam, HashMap<Integer, String> mapa) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		if(mapa.get(idParam) == null || mapa.get(idParam).equals(""))
			return null;
		return sdf.parse(mapa.get(idParam));
	}
	
	/**
	 * metodo que devuelve un booleano a partir del string contenido en un mapa de parametros
	 * @param idParam
	 * @param mapa
	 * @return
	 */
	public static Boolean getParamAsBoolean(Integer idParam, HashMap<Integer, String> mapa){
		if(mapa.get(idParam) == null || mapa.get(idParam).equals(""))
			return null;
		return Boolean.valueOf(mapa.get(idParam));
	}
	
	/**
	 * metodo que devuelve un Double a partir del string contenido en un mapa de parametros
	 * @param idParam
	 * @param mapa
	 * @return
	 */
	public static Double getParamAsDouble(Integer idParam, HashMap<Integer, String> mapa){
		if(mapa.get(idParam) == null || mapa.get(idParam).equals(""))
			return null;
		return Double.valueOf(mapa.get(idParam));
	}
	
	/**
	 * metodo que devuelve un Integer a partir del string contenido en un mapa de parametros
	 * @param idParam
	 * @param mapa
	 * @return
	 */
	public static Integer getParamAsInteger(Integer idParam, HashMap<Integer, String> mapa){
		if(mapa.get(idParam) == null || mapa.get(idParam).equals(""))
			return null;
		return Integer.valueOf(mapa.get(idParam));
	}
	
	/**
	 * metodo que devuelve un BigInteger a partir del string contenido en un mapa de parametros
	 * @param idParam
	 * @param mapa
	 * @return
	 */
	public static BigInteger getParamAsBigInteger(Integer idParam, HashMap<Integer, String> mapa){
		if(mapa.get(idParam) == null || mapa.get(idParam).equals(""))
			return null;
		return new BigInteger(mapa.get(idParam));
	}

	/**
	 * Metodo que carga el mapa de parametros desde la lista parametrosOperacion
	 * 
	 * @param operacion
	 * @return
	 */
//	public static Operacion loadParamMap(Operacion operacion){
//		/*
//		 * inicializa el mapa de parametros si es nulo y luego lo llena con los 
//		 * parametros enviados desde el front
//		 */
//		if(operacion.getParamMap() == null)
//			operacion.setParamMap(new HashMap<Integer, String>());
//		for(ParametrosOperacion parametro : operacion.getParametrosOperacions()){
//			operacion.getParamMap().put(parametro.getId().getIdParametro(), parametro.getValor());
//		}
//		return operacion;
//	}
	
	/**
	 * Busca en listado por identificador
	 * 
	 * @param id
	 */
	public static boolean existsInList(List<Perfil> lista, Perfil perfil) {
		if (lista.indexOf(perfil) != -1) {
			return true;
		}
		return false;
	}
	

}
