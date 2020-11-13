package co.odin.security;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class MasterSession {
	private static MasterSession instance;
	public static MasterSession instance() {
		if(instance == null)
			instance = new MasterSession();
		return instance;
	}
	private MasterSession(){}
	
	/**
	 * Map que contendrá: 
	 * <K> -> accessToken
	 * <V> -> Session
	 * Se usará este Map para validar la autorización de un cliente.
	*/
	@Getter
	private Map<String,Session> sessionTokenMap = new HashMap<String, Session>();
	
	
	/**
	 * Map que contendrá:
	 * <K> -> userId
	 * <V> -> session
	 * Se usará este Map para validar la autenticación de un cliente
	 */
	private Map<Integer,Object> sessionUserMap = new HashMap<Integer, Object>();
	
	/**
	 * Map que contendrá:
	 * <K> -> UserName
	 * <V> -> cantidad de intentos fallidos
	 * Se usará este Map para validar los intentos fallidos x cliente que intenta 
	 * autenticarse.
	 */
	@Getter
	private Map<String, Integer> sessionCounterMap = new HashMap<String, Integer>();
	
	/**
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public boolean containsKey(String key, String map){
		switch(map) {
			case "sessionCounter":
				return sessionCounterMap.containsKey(key);
			case "sessionUser":
				return sessionUserMap.containsKey(key);
			default:
				return sessionTokenMap.containsKey(key);
		}
	}
	
	public Object getValue(String key, String map){
		switch(map) {
			case "sessionCounter":
				return sessionCounterMap.get(key);
			case "sessionUser":
				return sessionUserMap.get(key);
			default:
				return sessionTokenMap.get(key);
		}
	}
	
	public void putEntry(String key, Object value, String map) {
		switch(map) {
			case "sessionCounter":
				sessionCounterMap.put(key, (Integer) value);
				break;
			case "sessionUser":
				sessionUserMap.put(new Integer(key),value);
				break;
			default:
				sessionTokenMap.put(key,(Session) value);
		}
	}
	
	public void removeKey(String key, String map) {
		switch(map) {
		case "sessionCounter":
			sessionCounterMap.remove(key);
			break;
		case "sessionUser":
			sessionUserMap.remove(new Integer(key));
			break;
		default:
			sessionTokenMap.remove(key);
	}
	}
}