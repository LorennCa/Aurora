package co.ias.teseo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase utilitaria encargada de manejar el cifrado de contraseñas.
 * Implementation of PasswordEncoder that uses the BCrypt strong hashing
 * function. Clients can optionally supply a "strength" (a.k.a. log rounds in
 * BCrypt) and a SecureRandom instance. The larger the strength parameter the
 * more work will have to be done (exponentially) to hash the passwords. The
 * default value is 10.
 *
 */
public class ShaPasswordEncoder {

	/**
	 * variable de tipo BCryptPasswordEncoderService para codificar y hacer match de
	 * claves
	 */
	
	private static BCryptPasswordEncoder pe = new BCryptPasswordEncoder(
			AppConstants.General.STRENGTH_PASSWORD_ENCODER);

	/**
	 * Codifica una cadena entrante para ser almacenada en base de datos
	 * 
	 * @param passwordToEncode
	 * @return
	 */
	public static String encode(String passwordToEncode) {
		return pe.encode(passwordToEncode);
	}

	/**
	 * Método encargado de recibir una cadena y compararla con una cadena ya
	 * codificada Si hace match, retorna true, en caso contrario, false
	 * 
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	public static boolean matches(String rawPassword, String encodedPassword) {
		return pe.matches(rawPassword, encodedPassword);
	}
	
	public static void main(String[] args) {
		System.out.println(pe.encode("xfiles"));
	}

}
