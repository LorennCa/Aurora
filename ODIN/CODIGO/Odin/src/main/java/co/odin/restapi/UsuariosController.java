package co.odin.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.common.entities.Perfil;
import co.common.entities.Usuario;
import co.common.entities.UsuarioPasswordHistorial;
import co.common.response.LoginResponse;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import co.common.response.Response;
import co.common.wrappers.AuthWrapper;
import co.common.wrappers.ClaveWrapper;
import co.common.wrappers.ConfirmPasswordWrapper;
import co.common.wrappers.UsuarioEntidadWrapper;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.enums.EstadosUsuarioEnum;
import co.odin.exception.UsuariosException;
import co.odin.security.MasterSession;
import co.odin.security.Session;
import co.odin.services.CerberoService;
import co.odin.services.PasswordDictionaryService;
import co.odin.services.PasswordResetService;
import co.odin.services.ProfileService;
import co.odin.services.UserService;
import co.odin.services.synchronize.UsuariosSynchronizeService;
import co.odin.utils.AppConstants;
import co.odin.utils.AppPaths;
import co.odin.utils.UsuariosPasswordEncoder;

import static co.odin.utils.AppConstants.*;
import static co.odin.utils.AppPaths.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(AppPaths.SERVICE_ROOT_CONTEXT)
@Api(value = "Kerberos principal controller")
public class UsuariosController {
	private static final Logger logger = LogManager.getLogger(UsuariosController.class);
	@Autowired
	private CerberoService cancerbero;
	
	@Autowired
	private UserService userService;
	
	private MessageSource mrc = MessageSourceConfig.messageSource();
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PasswordResetService prService;
	
	@Autowired
	private PasswordDictionaryService pdservice;
	
	@Autowired
	private UsuariosSynchronizeService cerberoSynhService;
	
	
	
	@ApiOperation(value = Security.ODIN_SECURITY_LOGIN_PATH, nickname = "login")
	@ApiResponses(value = {
			@ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = "Ok", response = LoginResponse.class),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_PARTIAL_CONTENT, message = "Mensaje de validación"),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Falla servidor") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "authwrapper", value = "JSON authwrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@PostMapping(value = AUTH_LOGIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody AuthWrapper authwrapper,
			HttpServletRequest request) throws Exception {

		logger.info("Solicitud de autenticación por: " + authwrapper.getUser());
		
		Response loginResponse = cancerbero.login(authwrapper, request);
		
		return ResponseEntity.status(loginResponse.getCode()).body(loginResponse);
	}
	
	@ApiOperation(value = Security.ODIN_SECURITY_LOGOUT_PATH, nickname = "logout", notes = "Se elimina una sesión de SEE_AdminService")
	@ApiResponses(value = {
			@ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = "Ok", response = Response.class),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_PARTIAL_CONTENT, message = "Mensaje de validación"),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Falla servidor") })
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "logoutWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@DeleteMapping(value = { AUTH_LOGOUT_PATH}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logout(HttpServletRequest request)
			throws Exception {
		try {
			Response logoutResponse = cancerbero.logout(request);
			return ResponseEntity.status(HttpStatus.OK).body(logoutResponse);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@ApiOperation(value = Usuarios.SEND_PASS_RESET_EMAIL, nickname = "sendReset", notes = "Se encarga del envío de correo con link de cambio de contraseña")
	@GetMapping(value = Usuarios.RESET_PASS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetUserPassword(HttpServletRequest request,
											   @RequestParam(value = "login", required = true) String login) {		
				
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		Usuario usuario = userService.findByLogin(login);
		
		if(usuario == null) {//Verificar si el correo existe, si no, no hacer nada.
			//res.setCode(HttpStatus.NOT_FOUND.ordinal());
			//res.setMessage("Solamente usuarios registrados pueden reestablecer la contraseña.");
			
			resourceResponse.setCode(HttpStatus.NOT_FOUND.value());
			resourceResponse.setMessage(mrc.getMessage("odin.resetPassword.forbidenReset.ERROR", null, null));
							
				otrasOpciones
						.setBusinessErrorCode(AppConstants.ODIN_RESET_NOT_FOUND);
				resourceResponse.setOtrasOpciones(otrasOpciones);
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceResponse);
		}
		
		if(!usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.ACTIVO.name())){
			//res.setCode(HttpStatus.NOT_FOUND.ordinal());
			//res.setMessage("Su usuario no se encuentra activo por favor contacte a su administrador");	
			/*
			 * Solo Usuarios activos pueden hacer restablecimiento de contraseña
			 */
			resourceResponse.setCode(HttpStatus.NOT_FOUND.value());
			resourceResponse.setMessage(mrc.getMessage("odin.resetPassword.invalidUserReset.ERROR", null, null));
							
				otrasOpciones
						.setBusinessErrorCode(AppConstants.ODIN_RESET_NOT_AUTHORIZED);
				resourceResponse.setOtrasOpciones(otrasOpciones);
			
	
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceResponse);
		}
		
		try {
			
			try {
				Response rres = prService.resetUserPassword(login, usuario.getEmail());
				logger.info(rres.getMessage());
				
				if (rres.getResource() == null ){
					throw new UsuariosException(rres.getMessage(), rres.getCode());
					
				}
				
				usuario.setCambioClave(true);
				usuario.setUltimoCambioClave(new Date());
				if (MasterSession.instance().getSessionCounterMap().containsKey(usuario.getLogin()))
					MasterSession.instance().getSessionCounterMap().put(usuario.getLogin(), 0);
				

					usuario.setPassword(UsuariosPasswordEncoder.encodePassword(rres.getResource(),//Este campo tiene la clave temporal, OTP. 
							            AppConstants.General.SALT));

					cerberoSynhService.saveUser(usuario);
				
			}
			catch(UsuariosException e) {
				
				logger.error(mrc.getMessage("odin.resetPassword.ExeptionReset.ERROR", null, null),
						"login ", login,
						e.getMessage());
				
				resourceResponse.setCode(e.getStatus().value());
				resourceResponse.setMessage(e.getMessage());
								
					otrasOpciones
							.setBusinessErrorCode(HttpStatus.CREATED.value());
					resourceResponse.setOtrasOpciones(otrasOpciones);
				
				return ResponseEntity.status(e.getStatus().value()).body(resourceResponse);
				
				
			}catch(Exception ex){
				logger.error(mrc.getMessage("odin.resetPassword.ExeptionReset.ERROR", null, null),
						"Login usuario", login,
						ex.getMessage());
				resourceResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				resourceResponse.setMessage(ex.getMessage());
				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
								resourceResponse);
			}
			
			resourceResponse.setMessage(mrc.getMessage("odin.resetPassword.sucessReset.SUCESS",null,null));
			
			return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
		}
		catch(Exception e) {
			logger.error("Excepción reinicializando contraseña para usuario " + usuario.getLogin());
			logger.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.CREATED).body(e.getLocalizedMessage());
		}		
	}
	
	@GetMapping(value = { ODIN_GENERAL_ACCESS_VALIDATE_TOKEN }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateToken(HttpServletRequest request) {
		ResourceResponse resourceResponse = new ResourceResponse();

		try {
			// envia nuevamente el accessToken y el uri
			resourceResponse.setAccessToken(request.getAttribute(
					General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse
					.setResource(ODIN_GENERAL_ACCESS_VALIDATE_TOKEN);

			// si va todo bien, envía respuesta OK
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);

			resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));

			logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
		} catch (Exception e) {
			resourceResponse
					.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.GET.ERROR",
					null, null));
			logger.error(mrc.getMessage("odin.recurso.GET.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	//@RequestMapping(value = { PASSWORD_HISTORY_CONTEXT }, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = { PASSWORD_HISTORY_CONTEXT_A }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> passwordValid(
			@RequestBody(required = true) ConfirmPasswordWrapper passwordWrapper,
			HttpServletRequest request) throws Exception {
		ResourceResponse resourceResponse = new ResourceResponse();

		try {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
			resourceResponse.setAccessToken(request.getAttribute(General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(Usuarios.PATH);
			
			if (passwordWrapper.getId() != null ) {
				if (null != passwordWrapper.getOldPass()
						&& !passwordWrapper.getOldPass().isEmpty()) {
					Usuario usuarioFound = userService.findOne(passwordWrapper.getId());
					if (UsuariosPasswordEncoder.validPassword(usuarioFound.getPassword(),
															 passwordWrapper.getOldPass(), General.SALT)) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
					}
					else {
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
					}
				}
				if (null != passwordWrapper.getNewPass() && !passwordWrapper.getNewPass().isEmpty()) {
					Usuario usuario = userService.findOne(passwordWrapper.getId());
					logger.info("Ultimas contraseñas usadas:\n".concat(usuario.getHistorialPassword().toString()));
					
					if (userService.passwordUnused(usuario, passwordWrapper.getNewPass())) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
					}
					else {
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
					}
				}
			}
			else {
				resourceResponse.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("odin.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}
		}
		catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("odin.recurso.GET.ERROR", null, null), "Usuario", passwordWrapper.getId(), e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	@ApiOperation(value = AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_PASSWORD_DICCIONARIO, nickname = "Diccionario", notes = "Valida si la palabra existe o no en el listado de las restringidas")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@RequestMapping(value = AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_PASSWORD_DICCIONARIO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	//@PostMapping(value= AppPaths.SEE_GENERAL_ACCESS_VALIDATE_PASSWORD_DICCIONARIO, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> validateClave(HttpServletRequest request,
			@RequestBody ClaveWrapper clave) throws Exception {

		ResourceResponse resourceResponse = new ResourceResponse();

		try {
			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE)
							.toString());
			resourceResponse.setResource(AppPaths.DiccionarioClaves.ODIN_DICCIONARIO_CLAVES);

			// Si viene la clave a valildar NO null
			if (null != clave.getClave() && !clave.getClave().isEmpty()
					&& clave.getClave().length() > 0) {
				// Si la clave a validar existe en las palabras restringidas, OK
				
				// Si la clave a validar existe en las palabras restringidas, OK

				if (pdservice.inRestrictedWords(clave.getClave())) {

					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
					resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK",
																null, null));
					logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
				}
				else {
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
					resourceResponse.setMessage(mrc.getMessage(
											"odin.security.autorizacion.resourceAccess.NOT_FOUND",
											null, null));
					logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
				}
			
			}
			else {
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				resourceResponse.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
				logger.info(mrc.getMessage("odin.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		} catch (Exception e) {
			logger.error(mrc.getMessage("odin.recurso.PUT.ERROR", null, null),"Diccionario Claves", " ", " ",
						 e.getMessage());
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.PUT.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	@RequestMapping(value = AppPaths.USER_PASSWORD_UPDATE_PASSWORD_CONTEXT, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePassword(
			@RequestBody(required = false) Usuario usuario, HttpServletRequest request,
			@PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id) {

		ResourceResponse resourceResponse = new ResourceResponse();
		Usuario usuarioFound = null;
		try {

			// Envía nuevamente el accessToken (De la sesión, puede que se haya
			// cambiado por mecanismo de Refreshtoken) y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE)
							.toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);

			if (null != id) {
				usuarioFound = userService.findOne(id);

				if (null != usuarioFound) {

					if (usuario.getId() != null) {

						if (usuario.getPassword() != null
								&& usuario.getPassword().length() > 0
								&& !usuario.getPassword().isEmpty()) {

							// Actualiza el password codificado
							usuarioFound.setPassword(UsuariosPasswordEncoder
										.encodePassword(usuario.getPassword(), AppConstants.General.SALT));

							// Agrega la contraseña actualizada al historial
							UsuarioPasswordHistorial newUsuPassHist = new UsuarioPasswordHistorial();
							newUsuPassHist.setFecha(new Date());
							newUsuPassHist.setPassword(usuarioFound
									.getPassword());
							newUsuPassHist.setUsuario(usuarioFound);
							usuarioFound.getHistorialPassword().add(
									newUsuPassHist);

							// Trae la información del usuario para guardar la
							// auditoría
							Session session = MasterSession.instance().getSessionTokenMap()
											  .get(resourceResponse.getAccessToken());

							/*
							 * Si el usuario quien actualiza la contraseña no es
							 * el mismo propietario de la misma, se procede a
							 * activar el cambio de contraseña oblgatorio
							 */

							if (usuario.getId().intValue() != session
									.getUsuario().getId().intValue()) {
								// Establece el cambio de clave obligatorio
								usuarioFound.setCambioClave(Boolean.TRUE);
							} else {
								// No es obligatorio el cambio de clave
								usuarioFound.setCambioClave(Boolean.FALSE);
							}

							// Audita la actualización de Usuario
							usuarioFound.setUserAudit(session.getUsuario()
									.getLogin());
							usuarioFound.setIpAudit(request.getAttribute(
									AppConstants.General.AUTORIZACION_IP)
									.toString());

							// Actualiza el último cambio de clave:
							usuarioFound.setUltimoCambioClave(new Date());

								/**
								 * Persiste el usuario conctándose únicamente a
								 * la BD interna
								 */
							//userService.save(usuarioFound);
							cerberoSynhService.saveUser(usuarioFound);
							

							logger.info(mrc
									.getMessage(
											"odin.security.autorizacion.resourceAccess.PUT.success",
											null, null));
							resourceResponse
									.setCode(org.apache.http.HttpStatus.SC_OK);
							resourceResponse
									.setMessage(mrc
											.getMessage(
													"odin.security.autorizacion.resourceAccess.PUT.success",
													null, null));
						}
					}
				}
			} else {
				logger.info(mrc.getMessage("odin.recurso.params.error", null,
						null));
				resourceResponse
						.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				resourceResponse.setMessage(mrc.getMessage(
						"odin.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						resourceResponse);
			}

		} catch (Exception e) {
			logger.info(mrc.getMessage("odin.recurso.PUT.ERROR", null, null),
					"Usuario", usuarioFound.getId(), usuarioFound.getNombres(),
					e.getMessage());
			resourceResponse
					.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(mrc.getMessage(
					"odin.security.autorizacion.resourceAccess.PUT.error", null,
					null)
					+ e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	
	@RequestMapping(value = {ODIN_GENERAL + AppPaths.VALIDATE_PROFILE_FOR_DELETE_CONTEXT
			+ AppPaths.ID_PARAM_VALUE }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validatePerfilEliminar(HttpServletRequest request,
			@PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id) {

		ResourceResponse resourceResponse = new ResourceResponse();
		
		try {

			// Envía nuevamente el accessToken (De la sesión, puede que se haya
			// cambiado por mecanismo de Refreshtoken) y el recurso
			resourceResponse.setAccessToken(request.getAttribute(ACCESS_TOKEN_CONST).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			/**
			 * Realiza la validación: Si por lo menos existe UN USUARIO que
			 * tenga un estado diferente a INACTIVO atado a este perfil no podrá
			 * ser eliminado dicho perfil
			 */

			if (id != null) {
				Perfil profileFound = profileService.findOne(id);

				if (profileFound != null) {
					int inactiveUsersNumber = userService.findByPerfilAndEstadoNot(profileFound,
									EstadosUsuarioEnum.INACTIVO.name()).size();
					// Si existe al menos un usuario diferente a INACTIVO, OK
					if (inactiveUsersNumber > 0) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse.setMessage(mrc.getMessage(
												"odin.security.autorizacion.resourceAccess.DELETE.success",
												null, null));
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.DELETE.success",
										null, null));
						return ResponseEntity.status(HttpStatus.OK).body(
								resourceResponse);
					}
					else {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
						resourceResponse.setMessage(mrc.getMessage(
												"odin.security.autorizacion.resourceAccess.NOT_FOUND",
												null, null));
						logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND",
										null, null));
					}
				}
			}
			else {
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				resourceResponse.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
				logger.info(mrc.getMessage("odin.recurso.params.error", null,
						null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						resourceResponse);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(mrc.getMessage("odin.perfil.DELETE.ERROR", null, null),"Perfil", id);
			resourceResponse.setAccessToken(null);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.DELETE.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
	}
	
	@RequestMapping(value = { ODIN_GENERAL + AppPaths.GENERAL_ACCESS_VALIDATE_USER_ENTITY_ID_CONTEXT }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public synchronized ResponseEntity<?> validateUsuarioDocumentoEntidad(
			HttpServletRequest request,
			@RequestBody UsuarioEntidadWrapper wrapper) {

		ResourceResponse resourceResponse = new ResourceResponse();
		try {
			

			/*
			 * Se envia 1 cuando es consulta al guardar 
			 * 0 cuando es consulta antes de guardar desde front*/
			resourceResponse = cancerbero.validateUserEntity(wrapper, 0 ); 
			// Envía nuevamente el accessToken (De la sesión, puede que se haya
			// cambiado por mecanismo de Refreshtoken) y el recurso
			resourceResponse.setAccessToken(request.getAttribute(ACCESS_TOKEN_CONST).toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);
			
			if(resourceResponse.getCode().equals(org.apache.http.HttpStatus.SC_OK)){				
				return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
			}else if(resourceResponse.getCode().equals(org.apache.http.HttpStatus.SC_NO_CONTENT)){
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
			}else{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		}
		catch (Exception e) {

			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.POST.ERROR", null, null));
			logger.error(mrc.getMessage("odin.recurso.POST.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		
	}
	
	@GetMapping(value = {AppPaths.ODIN_GENERAL + AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_DATE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateDate(HttpServletRequest request) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		
		// envia nuevamente el accessToken y el uri
		resourceResponse.setAccessToken(request.getAttribute(ACCESS_TOKEN_CONST).toString());
	    resourceResponse.setResource(AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_TOKEN);
		// si va todo bien, envía respuesta OK
		resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
		resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));

		logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
		
		Calendar today = Calendar.getInstance();
		logger.info("fecha Servidor " +  today.getTimeInMillis());
		otrasOpciones.setFechaServidor(today.getTimeInMillis());
		resourceResponse.setOtrasOpciones(otrasOpciones);
		
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	/**
	 * Devuelve el login de un usuario en sesión, si y sólo si existe un token de acceso para ese usuario.
	 * @param request
	 * @param accessToken
	 * @return
	 */
	@GetMapping(value = { KERBEROS_GET_USER_INSESSION_CONTEXT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserInSession(HttpServletRequest request,
			 @RequestParam(value = "accessToken", required = true) String accessToken) {
		Response res = null;
		if(MasterSession.instance().containsKey(accessToken, "sessionMap")) {
			Integer userIdInSession = ((Session)MasterSession.instance().getValue(accessToken, "sessionMap"))
							        .getUsuario().getId();
			logger.info("Usuario con ID " + userIdInSession + " en sesión");
			return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK.value(), "Usuario con ID " + userIdInSession + " en sesión",
										 accessToken, userIdInSession+""));
		}
		else
			res = new Response(HttpStatus.NOT_FOUND.value(), "No existe un usuario en sesión con el token indicado ",
							   accessToken, NO_USER_IN_SESSION_CONSTANT);
		
		logger.info(res.getMessage());
		return ResponseEntity.status(res.getCode()).body(res);
	}

	/**
	 * "Backdoor" para eliminar los intentos fallidos de login de un usuario, si existe.
	 * @param login
	 * @return
	 */
	@GetMapping(value = "/leto/{login}")
	public String resetUserCounter(@PathVariable(value = "login", required = true) String login ) {
		MasterSession.instance().putEntry(login, 0, "sessionCounter");
		return "User login attempts cleared, if any.";
	}
	
	/*
	 * Validacion perfiles hijos  
	 */
	//@ApiOperation(value = AppPaths.SEE_GENERAL + AppPaths.Usuarios.USUARIOS_VALIDAR_PERFILES_HIJOS, nickname = "usuarios", notes = "Trae toda la información de usuarios")
	@RequestMapping(value = {AppPaths.ODIN_GENERAL + AppPaths.Usuarios.USUARIOS_VALIDAR_PERFILES_HIJOS}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getChildrenProfiles(HttpServletRequest request) {

		ResourceResponse resourceResponse = new ResourceResponse();
		try{
			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);

			// Consulta la sesión en el Map por accessToken como llave
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

			// Debo consultar en base de datos por id de Usuario:
			// Se hace asi porque el usuarios está sujeto a actualizaciones
			Usuario usuario = userService.findOne(session.getUsuario().getId());
			
			List<Perfil> perfilesList = new ArrayList<Perfil>();
			
			if (!userService.esUsuarioEntidadSuperAdmin(usuario)
					|| !userService.esUsuarioPerfilSuperAdministrador(usuario)) {

				// Envía información parcial:
				// * Perfiles hijos de su perfil asignado
				// * Usuarios que son de su misma entidad

				perfilesList = profileService.findByPerfilSuperior(usuario
						.getPerfil());

				/**
				 * Se agrega el perfil que inició sesión
				 */
				perfilesList.add(usuario.getPerfil());
				/*
				 * Al no tener perfiles hijos, se envía codigo-mensaje de
				 * validación de negocio
				 */
				if (perfilesList.size() == 1) {
					resourceResponse
							.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);
					resourceResponse
							.setMessage(mrc
									.getMessage(
											"odin.GET.usuario.business.validation.childrenProfiles",
											null, null));
					logger.info(mrc
							.getMessage(
									"odin.GET.usuario.business.validation.childrenProfiles",
									null, null));
					return ResponseEntity
							.status(HttpStatus.PRECONDITION_FAILED).body(
									resourceResponse);
				}
			}

			
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.POST.success", null, null));
			logger.info(mrc
					.getMessage(
							"odin.security.autorizacion.resourceAccess.POST.success",
							null, null));
			return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
			
			
		}catch(Exception e){
			logger.info(mrc.getMessage("odin.recurso.POST.ERROR", null, null), e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);

			resourceResponse.setMessage(
					mrc.getMessage("odin.security.autorizacion.resourceAccess.POST.error", null, null) + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}


	}
	
	
	
}