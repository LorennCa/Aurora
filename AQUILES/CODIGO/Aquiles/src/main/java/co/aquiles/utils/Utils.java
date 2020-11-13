package co.aquiles.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utils {

	public static String getMayus() {
		Month mounth = LocalDate.now().getMonth();
		String nameMounth = mounth.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
		String firLet = nameMounth.substring(0, 1);
		String capital = firLet.toUpperCase();
		String deLet = nameMounth.substring(1, nameMounth.length());
		nameMounth = capital + deLet;
		return nameMounth;
	}

	public static double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		int nextRoundedValue = 100;
		resultado = Math.ceil(resultado/nextRoundedValue)*nextRoundedValue;
		return resultado;
	}

	public static File[] getFiles(String dir_path) {
		String files;
		File folder = new File(AppConstants.rutaServidorRef);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(".xls") || files.endsWith(".xlsx")) {
					System.out.println(files);
				}
			}
		}
		return listOfFiles;
	}

	public static String getFechaActual(String formato) {
		Date now = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat(formato);
		return formateador.format(now);
	}

	public static String formateaFecha(String fecha, String formato) throws ParseException {
		SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formateador = new SimpleDateFormat(formato);
		Date date = parseador.parse(fecha);
		return String.valueOf(formateador.format(date));
	}

	public static Properties getConfigs(String rutaProperties) throws FileNotFoundException, IOException {
		Properties configs = new Properties();
		configs.load(new FileInputStream(rutaProperties));
		return configs;
	}

	public static String convertirSHA256(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		byte[] hash = md.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();

		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	public static String getDate(String format) throws IllegalArgumentException {
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
		return DATE_FORMAT.format(today);
	}

	public static String generateFileName(String fileName, String fecha)
			throws IllegalArgumentException, ParseException {

		if (fileName.contains("{") && fileName.contains("}")) {
			int first = fileName.indexOf("{");
			int last = fileName.indexOf("}");
			String datePattern = fileName.substring(first + 1, last);
			String replace = fileName.substring(first, last + 1);
			String dateFormat = formateaFecha(fecha, datePattern);
			return fileName.replace(replace, dateFormat);
		}
		return fileName;
	}

	public static String generateFileNamePub(String fileName) throws IllegalArgumentException, ParseException {

		if (fileName.contains("{") && fileName.contains("}")) {
			int first = fileName.indexOf("{");
			int last = fileName.indexOf("}");
			String datePattern = fileName.substring(first + 1, last);
			String replace = fileName.substring(first, last + 1);
			String dateFormat = getDate(datePattern);
			return fileName.replace(replace, dateFormat);
		}
		return fileName;
	}

	public static String redondear(int decimales, double precio) {
		String val = precio + "";
		BigDecimal big = new BigDecimal(val);
		big = big.setScale(decimales, RoundingMode.HALF_UP);
		return "" + big;
	}

	public static String formatearTexto(String ceros, String texto) {
		String textoFormateado = String.format("%" + ceros + "s", texto).replace(' ', '0');
		return textoFormateado;
	}

	public static String sourcePaths(String ruta) throws IOException {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		InputStream is = classloader.getResourceAsStream("datasource.properties");

		Properties prop = new Properties();

		// prop.load(new
		// FileInputStream("src/main/resources/datasource.properties"));
		prop.load(is);

		String direccion = "";

		if (ruta == "RUTA_INVENTARIO") {
			return direccion = (String) prop.get("RUTA_INVENTARIO");
		} else if (ruta == "RUTA_IMAGEN") {
			return direccion = (String) prop.get("RUTA_IMAGEN");
		}else if (ruta == "NOMBRE_REDEPARTES") {
			return direccion = (String) prop.get("NOMBRE_REDEPARTES");
		}else if (ruta == "NIT_REDEPARTES") {
			return direccion = (String) prop.get("NIT_REDEPARTES");
		}else if (ruta == "DIRECCION") {
			return direccion = (String) prop.get("DIRECCION");
		}else if(ruta == "HOST"){
			return direccion = (String) prop.get("HOST");
		}else if(ruta == "PORT"){
			return direccion = (String) prop.get("PORT");
		}else if(ruta == "FROM"){
			return direccion = (String) prop.get("FROM");
		}else if(ruta == "PASSWORD"){
			return direccion = (String) prop.get("PASSWORD");
		}else if(ruta == "USERNAME"){
			return direccion = (String) prop.get("USERNAME");
		}else if(ruta == "RECIPIENT"){
			return direccion = (String) prop.get("RECIPIENT");
		}else if(ruta == "STARTTLS"){
			return direccion = (String) prop.get("STARTTLS");
		}
			else {
			direccion = (String) prop.get("RUTA_REFERENCIAS");
		}
		System.out.println("direccion-->" + direccion);
		return direccion;

	}

	// public static void main(String[] args) throws IOException {
	// System.out.println("resultado:" + convertirSHA256("root"));
	// }
}
