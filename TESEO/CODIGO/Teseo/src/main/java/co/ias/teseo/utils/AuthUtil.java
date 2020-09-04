package co.ias.teseo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthUtil {
	
	private static final SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
	
	public static String formatDate(long millis)
	{
		return df.format(new Date(millis));
	}
}