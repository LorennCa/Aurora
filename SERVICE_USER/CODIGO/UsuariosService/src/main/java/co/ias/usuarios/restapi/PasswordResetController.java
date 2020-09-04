package co.ias.usuarios.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ias.common.entities.DynamicLink;
import co.ias.common.response.Response;
import co.ias.usuarios.services.PasswordResetService;
import co.ias.usuarios.utils.AppPaths;


@RestController
@RequestMapping(AppPaths.PasswordReset.SERVICE_ROOT_CONTEXT_PASSRESET)
@Api(value = "Password reset controller")
public class PasswordResetController {
	private static final Logger logger = LogManager.getLogger(PasswordResetController.class);
	
	@Autowired
	private PasswordResetService prservice;

	@ApiOperation(value = AppPaths.PasswordReset.RESET_PASS_PATH, nickname = "resetPassword", notes = "Se encarga de recordar contraseña")
	@GetMapping(value = AppPaths.PasswordReset.RESET_PASS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetUserPassword(@RequestParam(value = "login", required = true) String login,
											   @RequestParam(value = "email", required = true) String email) {		
		try {
			Response rres = prservice.resetUserPassword(login, email);
			logger.info(rres.getMessage());
			return ResponseEntity.status(rres.getCode()).body(rres);
		}
		catch(Exception e) {
			logger.error("Excepción reinicializando contraseña para usuario " + login);
			logger.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}		
	}
	
	@GetMapping(value = AppPaths.PasswordReset.GET_PASSWORD_RESET_ATTEMPT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPasswordResetEntryByUserLogin(@RequestParam(value = "login", required = true) String login) {
		DynamicLink din = prservice.findPasswordReset(login);
		if(din == null) {
			logger.info("No existe una contraseña asociada a este usuario");
			return ResponseEntity.status(HttpStatus.OK).body(din);
		}
		else {
			logger.info("Existe una contraseña asociada a este usuario: " + din);
			return ResponseEntity.status(HttpStatus.OK).body(din);
		}
	}
	
	@DeleteMapping(value = AppPaths.PasswordReset.DELETE_PASSWORD_RESET_ATTEMPT_PATH, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> deletePasswordResetEntryByUser(@PathVariable("login") String login) {
		try {
			prservice.deletePasswordReset(login);
			return ResponseEntity.status(HttpStatus.OK).body("Eliminación de reestablecimiento de contraseña exitoso.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				   .body("Excepción eliminando reestablecimiento de contraseña:  " + e.getLocalizedMessage());
		}
	}
}