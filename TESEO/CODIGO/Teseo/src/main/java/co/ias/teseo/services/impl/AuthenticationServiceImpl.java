package co.ias.teseo.services.impl;

import static co.ias.teseo.utils.AppConstants.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.ias.teseo.config.appconfig.AuthApplicationConfiguration;
import co.ias.teseo.config.appconfig.Tokenizer;
import co.ias.teseo.model.Client;
import co.ias.teseo.model.User;
import co.ias.teseo.repository.ClientRepository;
import co.ias.teseo.repository.UserRepository;
import co.ias.teseo.response.OAuthResponse;
import co.ias.teseo.services.AuthenticationService;
import co.ias.teseo.utils.AuthUtil;


@Component
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private AuthApplicationConfiguration confs;
	
	private static Tokenizer tokenizer = Tokenizer.getInstance();
	
	private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
	
	@Override
	@Transactional(readOnly = true)
	public OAuthResponse authenticateUserByClient(String clientId, String inum, String user) {
		StringBuilder sb = new StringBuilder();
		OAuthResponse res = new OAuthResponse();
		Client cli = clientRepo.findOne(Integer.parseInt(clientId));
		if(cli == null)
			res.setCode(-1);
		else
		{
			if(!cli.getAppInum().equals(inum))
				res.setCode(-2);
			else
			{
				User appUserFound = userRepo.findByUser(user);
				if(appUserFound == null)
					res.setCode(-3);
				else
				{
					res.setCode(HttpStatus.OK.value());
					String accessToken = tokenizer.addToken(appUserFound);
					sb.append("User ").append(appUserFound).append(" tokenized at ").append(AuthUtil.formatDate(System.currentTimeMillis()));
					res.setMessage(sb.toString());
					res.setAccessToken(accessToken);
					logger.info(res.getMessage());
				}
			}
		}
		res.setExpireTime(confs.getExpiretime());
		sb = null;
		return res;
	}

	@Override
	public OAuthResponse refreshToken(String username) {
		StringBuilder sb = new StringBuilder();
		OAuthResponse res = new OAuthResponse();
		User uss = userRepo.findByUser(username);
		if(uss == null || !tokenizer.userExists(username))
			res.setCode(-3);
		else
		{
			res.setCode(0);
			String accessToken = tokenizer.refreshToken(uss);
			sb.append("User ").append(username).append(" token-refreshed at ").append(AuthUtil.formatDate(System.currentTimeMillis()));
			res.setMessage(sb.toString());
			res.setAccessToken(accessToken);
			logger.info(res.getMessage());
		}
		res.setExpireTime(confs.getExpiretime());
		sb = null;
		return res;
	}

	@Override
	@Transactional
	public OAuthResponse createUser(String clientId, String inum, String username, MessageSource mrc) {
		StringBuilder sb = new StringBuilder();
		OAuthResponse res = new OAuthResponse();
		Client cli = clientRepo.findOne(Integer.parseInt(clientId));
		if(cli == null)
		{
			res.setCode(-1);
			res.setMessage("Aplicación no registrada");
		}
		else
		{
			if(!cli.getAppInum().equals(inum))
			{
				res.setCode(-2);
				res.setMessage("Credenciales de aplicación inválidas");
			}
			else
			{
				res.setCode(HttpStatus.OK.value());
				User userAlready = userRepo.findByUser(username);
				if(userAlready == null) {
					User newUser = userRepo.save(new User(username));
					sb.append("User ").append(newUser.getApp_user_name()).append(" created on ")
					  .append(AuthUtil.formatDate(System.currentTimeMillis()));
				}
				else
					sb.append(mrc.getMessage("authteseo.auth.useralreadyonoauth", null, null));
				
				res.setMessage(sb.toString());
				res.setAccessToken(NO_TOKEN_STRING_CONSTANT);
				logger.info(res.getMessage());
			}
		}
		sb = null;
		return res;
	}
	
	@Override
	@Transactional
	public OAuthResponse deleteUser(String clientId, String inum, String username) {
		StringBuilder sb = new StringBuilder();
		OAuthResponse res = new OAuthResponse();
		Client cli = clientRepo.findOne(Integer.parseInt(clientId));
		if(cli == null)
		{
			res.setCode(-1);
			res.setMessage("Aplicación no registrada");
		}
		else
		{
			if(!cli.getAppInum().equals(inum))
			{
				res.setCode(-2);
				res.setMessage("Credenciales de aplicación inválidas");
			}
			else
			{
				userRepo.deleteByAppUserName(username);
				res.setCode(HttpStatus.OK.value());
				tokenizer.removeUser(username);
				sb.append("User ").append(username).append(" deleted on ")
				  .append(AuthUtil.formatDate(System.currentTimeMillis()));
				res.setMessage(sb.toString());
				res.setAccessToken(NO_TOKEN_STRING_CONSTANT);
				logger.info(res.getMessage());
			}
		}
		sb = null;
		return res;
	}
}