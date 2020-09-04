package co.ias.teseo.utils;

public interface AppPaths {

	public static final String REST_API_GENERAL = "/teseoAuth/v1";
	public static final String GET_TOKEN_PATH = "/gettoken/{clientId}/{inum}/{user}";
	public static final String NEW_USER = "/newuser/{clientId}/{inum}/{username}";
	public static final String ERASE_USER = "/eraseuser/{clientId}/{inum}/{username}";
	public static final String REFRESH_TOKEN = "/refreshtoken/{username}";	
}