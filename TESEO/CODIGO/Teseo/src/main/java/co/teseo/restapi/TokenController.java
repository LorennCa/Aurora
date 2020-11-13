/**
 * 
 */
package co.teseo.restapi;

import io.swagger.annotations.Api;
import co.teseo.config.appconfig.MessageSourceConfig;
import co.teseo.response.OAuthResponse;
import co.teseo.services.AuthenticationService;
import co.teseo.utils.AppPaths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPaths.REST_API_GENERAL)
@Api(value = "Token Controller")
public class TokenController {
	private static final Logger logger = LogManager.getLogger(TokenController.class);
	private MessageSource mrc = MessageSourceConfig.messageSource();
		
	@Autowired
	private AuthenticationService authService;
	
	@GetMapping("")
	public ResponseEntity<?> welcome() {
		return ResponseEntity.status(HttpStatus.OK).
				body("<div align=\"center\"><strong>OAuth SEE token authenticator REST API</strong></div>");
	}
		
	@GetMapping(value = AppPaths.GET_TOKEN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getToken(@PathVariable(value = "clientId", required = true) String clientId,
						   @PathVariable(value = "inum", required = true) String inum,
						   @PathVariable(value = "user", required = true) String user)
	{			
		logger.info("Client requesting token with this info:");
		logger.info("clientId: " + clientId);
		logger.info("inum: " + inum);
		logger.info("user: " + user);
		
		OAuthResponse resp = authService.authenticateUserByClient(clientId, inum, user);
		
		if(resp.getCode().equals(-1)) {
			logger.info(mrc.getMessage("authteseo.auth.noclientid", null, null));
			ResponseEntity<?> rentboy = ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
			return rentboy;
		}
		else if(resp.getCode().equals(-2)) {
			logger.info(mrc.getMessage("authteseo.auth.clientinvalidcredentials", null, null));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
		}
		else if(resp.getCode().equals(-3)) {
			logger.info(mrc.getMessage("authteseo.auth.unauthorizeduser", null, null));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
		}
		else {
			logger.info(mrc.getMessage("authteseo.auth.gettoken.success", null, null));
			ResponseEntity<?> rentboy = ResponseEntity.status(HttpStatus.OK).body(resp);
			return rentboy;
		}
	}
	
	@PostMapping(value = AppPaths.NEW_USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@PathVariable(value = "clientId", required = true) String clientId,
			   @PathVariable(value = "inum", required = true) String inum,
			   @PathVariable(value = "username", required = true) String username)
	{
		logger.info("\nClient requesting user creation:\nclientId: " + clientId);
		logger.info("inum: " + inum);
		logger.info("user: " + username);
		
		OAuthResponse resp = authService.createUser(clientId, inum, username, mrc);
		if(resp.getCode().equals(-1)) {
			logger.info(mrc.getMessage("authteseo.auth.noclientid", new Object[]{"",clientId}, null));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}										 
		else if(resp.getCode().equals(-2)) {
			logger.info(mrc.getMessage("authteseo.auth.clientinvalidcredentials", null, null));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
		}
		else {
			logger.info(mrc.getMessage("authteseo.auth.createuser.success", null, null));
			return ResponseEntity.status(HttpStatus.OK).body(resp);
		}
	}
	
	@DeleteMapping(value = AppPaths.ERASE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUser(@PathVariable(value = "clientId", required = true) String clientId,
			   @PathVariable(value = "inum", required = true) String inum,
			   @PathVariable(value = "username", required = true) String username)
	{
		logger.info("\nClient requesting user deletion:\nclientId: " + clientId);
		logger.info("inum: " + inum);
		logger.info("user: " + username);
		
		OAuthResponse resp = authService.deleteUser(clientId, inum, username);
		if(resp.getCode().equals(-1)) {
			logger.info(mrc.getMessage("authteseo.auth.noclientid", new Object[]{"",clientId}, null));
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}
		else if(resp.getCode().equals(-2))	{
			logger.info(mrc.getMessage("authteseo.auth.clientinvalidcredentials", null, null));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
		}
		else {
			logger.info(mrc.getMessage("authteseo.auth.deleteuser.success", null, null));
			return ResponseEntity.status(HttpStatus.OK).body(resp);
		}
	}
	
	@GetMapping(AppPaths.REFRESH_TOKEN)
	public ResponseEntity<?> refreshToken(@PathVariable(value = "username", required = true) String username)
	{			
		logger.info("\nClient requesting token refresh for user: " + username);		
		OAuthResponse resp = authService.refreshToken(username);
		
		if(resp.getCode().equals(-3)) {
			logger.info(mrc.getMessage("authteseo.auth.unauthorizeduser", null, null));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
		}
		else {
			logger.info("Token actualizado para usuario " + username);
			ResponseEntity<?> rentboy = ResponseEntity.status(HttpStatus.OK).body(resp);			
			return rentboy;
		}
	}
}