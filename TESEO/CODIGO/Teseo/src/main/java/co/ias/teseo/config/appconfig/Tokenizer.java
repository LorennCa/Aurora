package co.ias.teseo.config.appconfig;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.ias.teseo.model.User;


public class Tokenizer
{	
	/**
	 * Single instance of this tokenizer
	 */
	private static Tokenizer instance = null;
	
	/**
	 * Users red-black tree with their corresponding token/token-creation date map
	 */
	private static final SortedMap<String, Map<String, Date>> userTokenRedBlackTree = 
						 Collections.synchronizedSortedMap(new TreeMap<String, Map<String, Date>>());
	
	/**
	 * Cryptographic key file, statically defined relative to this project
	 */
	@SuppressWarnings("unused")
	private static InputStream authKeyFis = null;
	
	/**
	 * Class logger
	 */
	private static final Logger logger = LogManager.getLogger(Tokenizer.class);
	
	/**
	 * Singleton implementation
	 * @return
	 */
	public static Tokenizer getInstance(){
		if(instance == null)
			instance = new Tokenizer();
		return instance;
	}
	
	/**
	 * Static initalization of the cryptographic key file
	 */
	static{
		try {
			//authKeyFis = new FileInputStream("authSee.jks");
			authKeyFis = new FileInputStream("/iasConfigFiles/authSee.jks");
		} catch (FileNotFoundException e) {			
			logger.error("Cryptographic key file not found.\n" + e.getMessage());
		}
	}
	
	/**
	 * Singleton implementation 
	 */
	private Tokenizer(){}
	
	/**
	 * Creates an access token based on the username, a returns it
	 * @param user User requested by client app for access tokenization 
	 * @return A JSON Web Token
	 */
	public String addToken(User user)
	{
		String accessToken = null;
		synchronized (userTokenRedBlackTree)
		{			
			accessToken = getAccessToken(user);
			if(userTokenRedBlackTree.containsKey(user.getApp_user_name()))
				userTokenRedBlackTree.get(user.getApp_user_name()).put(accessToken, Calendar.getInstance().getTime());
			else
			{
				Map<String, Date> userTokensMap = new java.util.HashMap<String, Date>();
				userTokensMap.put(accessToken, Calendar.getInstance().getTime());
				userTokenRedBlackTree.put(user.getApp_user_name(), userTokensMap);
			}
		}
		return accessToken;
	}

	/**
	 * Opens up a cryptographic key file, creates a JSON Web Signature, sign it, compress it and compact it so a JWT is
	 * returned.
	 * @param user The user whose username is used as a registered claim subject to create the JWT. This user has to be
	 * registered in the appUser table.
	 * @return A compact string representing the JWT.
	 */
	private String getAccessToken(User user)
	{
		String accessToken = null;		
		Key cryptoKey;
		ObjectInputStream oin = null;
		InputStream authKeyFis = null;
		try
		{
			//authKeyFis = new FileInputStream("authSee.jks");
			authKeyFis = new FileInputStream("/iasConfigFiles/authSee.jks");
			oin = new ObjectInputStream(authKeyFis);
			cryptoKey = (Key) oin.readObject();			
			accessToken = Jwts.builder().setSubject(user.getApp_user_name()+System.currentTimeMillis()).compressWith(CompressionCodecs.DEFLATE)
						  .signWith(SignatureAlgorithm.HS256, cryptoKey)
				    	  .compact();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("Exception while opening cryptographic key file or class not found while reading crypto-key "
					+ "file\n" + e.getMessage());
		} 
		finally {
		  try {
			oin.close();
		} catch (IOException e) {
				logger.error("Exception while closing crypto-key file object input stream\n" + e.getMessage());
			}
		}
		//Jwts.parser().setSigningKey(cryptoKey).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe");
		return accessToken;
	}

	public boolean userExists(String username) {
		return userTokenRedBlackTree.containsKey(username);
	}

	public String refreshToken(User uss) {
		String accessToken = getAccessToken(uss);
		synchronized (userTokenRedBlackTree) {
			userTokenRedBlackTree.remove(uss.getApp_user_name());
			Map<String, Date> userTokensMap = new java.util.HashMap<String, Date>();
			userTokensMap.put(accessToken, Calendar.getInstance().getTime());
			userTokenRedBlackTree.put(uss.getApp_user_name(), userTokensMap);
			return accessToken;
		}
	}

	public void removeUser(String username) {
		synchronized (userTokenRedBlackTree) {
			userTokenRedBlackTree.remove(username);
		}
	}
}