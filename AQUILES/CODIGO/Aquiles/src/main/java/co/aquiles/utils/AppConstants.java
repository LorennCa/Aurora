package co.aquiles.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Clase que contiene los valores constantes de la aplicación
 * 
*
 *
 * @author SandraL_Cardona
 */

public interface AppConstants {
	
	Date date = new Date();
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);

	public static final String THIS_SERVICE_NAME = "Aquiles";
	public static final String ACCESS_TOKEN_VALUE = "accesstoken";
	
    public static final String CONST_COMPONENT_PROPERTIES = "user.properties";
    public static final String CONST_COMPONENT_PROPERTIES_USER = "USER";
    public static final String CONST_COMPONENT_PROPERTIES_PASS = "PASSWORD";
    public static final String HOST = "54.191.190.145";
    public static final String PORT = "3306";
    public static final String NAME = "sisval2";
    public static final String USR = "lcardona_bk";
    public static final String PASS = "lc4rd0n4";
        
    
//    public static final String pathOpe = "C:\\INFO_VALORACION\\Renta_Variable\\PRUEBA\\";
//    public static final String pathInd = "C:\\INFO_VALORACION\\Renta_Variable\\PRUEBA\\";
//    public static final String pathPrecios = "C:\\INFO_VALORACION\\Renta_Variable\\RENTA_VARIABLE_INTERNACIONAL\\INSUMOS\\";
//    public static final String pathReuters = "C:\\INFO_VALORACION\\Renta_Variable\\RENTA_VARIABLE_INTERNACIONAL\\INSUMOS\\prueba.csv";
//    public static final String FILENAME = "C:\\INFO_VALORACION\\ARCHIVOS\\MX{MMddyy}_RV_FormatoLocal.txt";
//    public static final String FILE_NAME_MX_RV = "C:\\INFO_VALORACION\\ARCHIVOS\\MX{MMddyy}_RV.txt";
//    public static final String FILE_reuters = "C:\\INFO_VALORACION\\Renta_Variable\\RENTA_VARIABLE_INTERNACIONAL\\INSUMOS\\DATOS_REUTERS.xlsx";
    
    
    
    public static final String QUERY_TOTAL_OPERATIONS = "SELECT i_folio, c_nemotecnico FROM va_operacion WHERE d_fechaoperacion = '#';";
    public static final String QUERY_REMOVE_OPERATIONS = "SELECT i_folio, c_nemotecnico FROM va_operacion WHERE substring(i_folio,1,1)=7 AND d_fechaoperacion = '#';";
    public static final String QUERY_INDEX_ICOLCAP = "SELECT c_nemotecnico,c_marcacion, e_precio, dt_fechahora FROM va_operacion WHERE (c_nemotecnico = 'ICOLCAP' AND c_marcacion = 'S' AND d_fechaoperacion = '#' AND i_folio =(SELECT MAX(i_folio) FROM va_operacion WHERE c_nemotecnico = 'ICOLCAP' AND d_fechaoperacion = '#' AND c_marcacion = 'S'))";
    public static final String QUERY_INDEX_HCOLSEL = "SELECT c_nemotecnico,c_marcacion, e_precio, dt_fechahora FROM va_operacion WHERE (c_nemotecnico = 'HCOLSEL' AND c_marcacion = 'S' AND d_fechaoperacion = '#' AND i_folio =(SELECT MAX(i_folio) FROM va_operacion WHERE c_nemotecnico = 'HCOLSEL' AND d_fechaoperacion = '#' AND c_marcacion = 'S'))";
    public static final String QUERY_INDEX_ICOLRISK = "SELECT c_nemotecnico,c_marcacion, e_precio, dt_fechahora FROM va_operacion WHERE (c_nemotecnico = 'ICOLRISK' AND c_marcacion = 'S' AND d_fechaoperacion = '#' AND i_folio =(SELECT MAX(i_folio) FROM va_operacion WHERE c_nemotecnico = 'ICOLRISK' AND d_fechaoperacion = '#' AND c_marcacion = 'S'))";
    public static final String QUERY_UPDATED_INDICES = "SELECT c_nemotecnico, e_preciovaloracion, d_fecha FROM va_preciovaloracion WHERE d_fecha = '#' AND c_nemotecnico IN ('ICOLCAP','ICOLRISK','HCOLSEL')";
    public static final String QUERY_GENERATE_FILE = "select pais, emisor, isin, ticker, moneda,  tipo_instrumento,  precio, mercado, dias_vencimiento, fecha from va_preciosint where fecha = '#';";
    public static final String QUERY_INTERNATIONAL_PRICES = "select pais, emisor, isin, ticker, moneda,  tipo_instrumento,  precio, mercado, dias_vencimiento, fecha from va_preciosint where fecha = ('#' - INTERVAL 1 DAY);";
    public static final String QUERY_ISINES_NUEVOS = "select pais, emisor, isin, ticker, moneda,  tipo_instrumento,  precio, mercado, dias_vencimiento, fecha from va_preciosint where fecha = ('#' - INTERVAL 2 DAY);";
    

    public static final String ERROR_OPE_FILE = "ERROR - EXISTEN MAS REGISTROS EN XSTREAM";
    public static final String ERROR_OPE_VAL = "ERROR - EXISTEN MAS REGISTROS EN EL VALORADOR";
    public static final String ERROR_NO_OPERATIONS = "ALGUNA DE LAS FUENTES NO TIENE REGISTROS";
    public static final String UPDATED_INDICES = "INDICES ACTUALIZADOS CORRECTAMENTE";
    public static final String INCORRECT_INDICES = "LOS PRECIOS EN LOS INDICES NO COINDEN EN LAS FUENTES";
    public static final String COMPLETE_OPERATIONS = "INFORMACION COMPLETA";
    public static final String INDICE_NO_MARCA_ENDB = "EL INDICE NO MARCO PRECIO EN EL VALORADOR";
    public static final String EXCLUDED_OPERATIONS = "OPERACIONES EXCLUIDAS DE MANERA EXITOSA";
    public static final String UPDATE_PRICES = "PRECIOS ACTUALIZADOS CORRECTAMENTE";
    
    public static final String INDEX_FILE_NAME = "X-stream.csv";
    public static final String CSV_SEPARATOR = ",";
    public static final String directorio = "C:\\Users\\Lorena\\Documents\\";
    public static final String rutaServidorRef = "C:\\Users\\Lorena\\Documents\\referencias.xlsx";
    public static final String rutaServidorInv = "C:\\Users\\Lorena\\Documents\\inventario.xlsx";
    
    public static final String formatDate = "yyyyMMdd";
    public static final String formatDateDiv = "yyyy-MM-dd";
    public static final String HEADER_PRICES_PUBLICATION = 
            "Fecha Valoracion" + CSV_SEPARATOR
            + "Pais" + CSV_SEPARATOR
            + "Emisor" + CSV_SEPARATOR
            + "ISIN" + CSV_SEPARATOR
            + "Moneda" + CSV_SEPARATOR
            + "Tipo de Instrumento" + CSV_SEPARATOR
            + "Precio" + CSV_SEPARATOR
            + "Mercado";
    
    public static final String ORIGEN_VALORADOR = "VALORADOR";
    public static final String ORIGEN_XSTREAM= "XSTREAM";
    public static final String CONSECUTIVO = "00001";
    public static final String C = "C";
    public static final String SPV = "SPV";
    public static final String NIT = "9004093630";
    public static final String D = "D            ";
    public static final String LONG = "000000000000000000";
    public static final String CER = "00000000";
    public static final String ESP = "      +";
    public static final String FORM = "000000000.0000";
    public static final String ESPA = "        ";
    public static final String RADOS = "            ";
    public static final String ESPE = "            ";
    
    
    
}