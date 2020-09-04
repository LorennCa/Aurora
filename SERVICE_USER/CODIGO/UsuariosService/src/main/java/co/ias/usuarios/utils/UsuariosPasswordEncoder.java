package co.ias.usuarios.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.ias.usuarios.exception.UsuariosException;


public class UsuariosPasswordEncoder {

	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(UsuariosPasswordEncoder.class);

	/**
	 * Codifica la cadena plana entrante
	 * 
	 * @param rawPass
	 * @param salt
	 * @return
	 * @throws EEException
	 */
	public static String encodePassword(String rawPass, Object salt) throws UsuariosException {

		String output = "";
		try {
			String saltString = (String) salt;
			SecretKeySpec key = new SecretKeySpec(saltString.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
					saltString.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			byte[] ecrypted = cipher.doFinal(rawPass.getBytes());
			output = DatatypeConverter.printBase64Binary(ecrypted);

		} catch (InvalidKeyException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (IllegalBlockSizeException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (BadPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (NoSuchPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		}
		return output;
	}

	/**
	 * Retorna true si la contraseña plana entrante al codificarla coincide con
	 * la codificada que entra como parámetro
	 * 
	 * @param encPass
	 * @param rawPass
	 * @param salt
	 * @return
	 * @throws EEException
	 */
	public static boolean validPassword(String encPass, String rawPass,
			Object salt) throws Exception {

		try {
			String saltString = (String) salt;
			SecretKeySpec key = new SecretKeySpec(saltString.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
					saltString.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			byte[] ecrypted = cipher.doFinal(rawPass.getBytes());
			String output = DatatypeConverter.printBase64Binary(ecrypted);
			if (encPass.equals(output)) {
				return true;
			}

		} catch (InvalidKeyException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} catch (NoSuchPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} catch (IllegalBlockSizeException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} catch (BadPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		}
		return false;
	}
	
	public static boolean isPasswordValid(String encPass, String rawPass,
			Object salt) throws UsuariosException {

		try {
			String saltString = (String) salt;
			SecretKeySpec key = new SecretKeySpec(saltString.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
					saltString.getBytes());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			byte[] ecrypted = cipher.doFinal(rawPass.getBytes());
			String output = DatatypeConverter.printBase64Binary(ecrypted);
			if (encPass.equals(output)) {
				return true;
			}

		} catch (InvalidKeyException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (NoSuchPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (IllegalBlockSizeException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		} catch (BadPaddingException ex) {
			logger.error(ex.getMessage(), ex);
			throw new UsuariosException(ex.getMessage());
		}
		return false;
	}
}
