package co.ias.teseo.services;

import org.springframework.context.MessageSource;

import co.ias.teseo.response.OAuthResponse;

public interface AuthenticationService {

	public OAuthResponse authenticateUserByClient(String clientId, String inum, String user);

	public OAuthResponse createUser(String clientId, String inum, String username, MessageSource mrc);
	
	public OAuthResponse deleteUser(String clientId, String inum, String username);

	public OAuthResponse refreshToken(String username);
}