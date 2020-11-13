
package co.odin.services.impl;

import static co.odin.utils.AppConstants.NO_USER_IN_SESSION_CONSTANT;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import co.common.custom.MenuTemplate;
import co.common.entities.AuditAuth;
import co.common.entities.AuditAuthFallos;
import co.common.entities.DynamicLink;
import co.common.entities.IpPermitida;
import co.common.entities.Usuario;
import co.common.response.LoginResponse;
import co.common.response.OAuthResponse;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import co.common.response.Response;
import co.common.wrappers.AuthWrapper;
import co.common.wrappers.UsuarioEntidadWrapper;
import co.odin.config.appconfig.AppConfiguration;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.enums.EstadosUsuarioEnum;
import co.odin.enums.TipoEntidadEnum;
import co.odin.repositories.PasswordHistorialOwnRepository;
import co.odin.security.MasterSession;
import co.odin.security.Session;
import co.odin.services.AuditAuthFallosService;
import co.odin.services.AuditAuthService;
import co.odin.services.CerberoService;
import co.odin.services.PasswordResetService;
import co.odin.services.UserService;
import co.odin.utils.AppConstants;
import co.odin.utils.AppPaths;
import co.odin.utils.HermesMachine;
import co.odin.utils.UsuariosPasswordEncoder;
import co.odin.utils.UsuariosUtil;

@Component
public class UsuariosServiceImpl implements CerberoService {

	private static final Logger logger = LogManager.getLogger(UsuariosServiceImpl.class);
	private MessageSource mrc = MessageSourceConfig.messageSource();
	@Autowired
	private Environment springEnvironment;
	@Autowired
	private UserService userService;
	@Autowired
	private AuditAuthService auditAuthService;
	@Autowired
	private AuditAuthFallosService auditAuthFallosService;
	@Autowired
	private PasswordResetService prService;
	@Autowired
	private PasswordHistorialOwnRepository passwordHistorialOwnRepository;
	
	

	@Override
	public Response login(AuthWrapper authwrapper, HttpServletRequest request) {
		Response messageResponse = new Response();
		messageResponse.setCode(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
		try {

			Usuario usuario = userService.findByLogin(authwrapper.getUser());
			String clientIp = UsuariosUtil.getIPFromRequest(request);
			
			/** Sólo usuarios existentes **/
			if (usuario != null) {
				/** Usuarios que no tengan contraseña expirada en "olvidé mi contraseña" **/
				if(validPasswordReset(usuario)) {
					if (activeUser(usuario)) {
					
						/** Contraseña correcta**/
						if (usuario != null && UsuariosPasswordEncoder.validPassword(usuario.getPassword(),
							authwrapper.getPassword(), AppConstants.General.SALT)) {
							boolean validateIPActive = AppConfiguration.intance().getSeeConfig().isValidarIP();

							if (ipInWhiteList(clientIp, usuario.getListIpPermitidas()) && validateIPActive
								|| (!validateIPActive)) {
								OAuthResponse oauthResponse = getAccessToken(usuario);
								if (oauthResponse != null && oauthResponse.getAccessToken() != null
									&& !oauthResponse.getAccessToken().isEmpty()) {
									logger.info(mrc.getMessage(
												"odin.security.autenticacion.tokenclient.crear.success", null,
												null));
									MenuTemplate menuTemplate = AppConfiguration.intance()
																.getMenuGeneralXPerfilMap()
																.get(usuario.getPerfil().getId());
									
									LoginResponse loginResponse = login(oauthResponse, usuario, clientIp, menuTemplate);
									
									if (MasterSession.instance().containsKey(authwrapper.getUser(),"sessionCounter"))
										MasterSession.instance().putEntry(authwrapper.getUser(), new Integer(0), "sessionCounter");
									
									logger.info("User: " + authwrapper.getUser() + " " + mrc
											.getMessage("odin.security.autenticacion.usuario.login.success", null, null));

									return loginResponse;
								}
								else {
									String errorMessage = null;
									if (oauthResponse != null && oauthResponse.getMessage() != null) {
										logger.info(mrc.getMessage("odin.security.autenticacion.token.error", null, null)
												+ " : " + oauthResponse.getMessage());
										if (oauthResponse.getCode() == org.apache.http.HttpStatus.SC_UNAUTHORIZED) {
											errorMessage = mrc.getMessage(
													"odin.security.autenticacion.tokenclient.unauthorized.error", null,
													null);
										}
									}
									else {
										logger.info(
											mrc.getMessage("odin.security.autenticacion.token.error", null, null) + " : "
														+ mrc.getMessage(
																"odin.security.autenticacion.tokenclient.timeout.error",
																null, null));
										errorMessage = mrc.getMessage(
												"odin.security.autenticacion.tokenclient.timeout.error", null, null);
									}
									messageResponse.setMessage(mrc.getMessage("odin.security.autenticacion.token.error",
															   null, null) + " - " + errorMessage);
									messageResponse.setCode(org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE);
									logger.info(messageResponse.getMessage());
									return messageResponse;
								}
							}
							else {
								messageResponse.setMessage(bloquearUsuario(userService, authwrapper,
										AppConstants.General.BLOQUEO_IP_PERMITIDA));
								auditFailLogin(authwrapper,
										mrc.getMessage("odin.security.autenticacion.usuario.IP.error", null, null), 
													   clientIp);
							}
						}
						else {
							if (MasterSession.instance().containsKey(authwrapper.getUser(),"sessionCounter")) {
								if (!validateAllowedAttempts(authwrapper.getUser())) {
									messageResponse.setMessage(bloquearUsuario(userService, authwrapper,
											AppConstants.General.BLOQUEO_INTENTOS_PERMITIDOS));

								} else {
									MasterSession.instance().putEntry(authwrapper.getUser(),
											MasterSession.instance().getSessionCounterMap()
														 .get(authwrapper.getUser()) + 1,"sessionCounter");
									messageResponse.setMessage(
										mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
									logger.info(
										mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));

									// Audita el fallido intento de inicio de sesión
									// por usuario o contraseña no válidos
									auditFailLogin(authwrapper,
											mrc.getMessage("odin.security.autenticacion.usuario.login.error", null,
															null), clientIp);
								}
							}
							else {//lo ingresa x primera vez en el mapa de intentos fallidos
								MasterSession.instance().putEntry(authwrapper.getUser(), new Integer(1),"sessionCounter");
								messageResponse.setMessage(
									mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
								auditFailLogin(authwrapper,
									mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null),
												   clientIp);
								logger.error(
									mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
							}
						}
				}
				else {
					if (usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.BLOQUEADO.name())) {
						messageResponse.setMessage(
						mrc.getMessage("odin.security.autenticacion.usuario.bloqueado.error", null, null));
						logger.error(mrc.getMessage("odin.security.autenticacion.usuario.bloqueado.error", null, null));
					}
					else {
						messageResponse.setMessage(
							mrc.getMessage("odin.security.autenticacion.usuario.inactivo.error", null, null));
						auditFailLogin(authwrapper,
							mrc.getMessage("odin.security.autenticacion.usuario.inactivo.error", null, null), clientIp);
						logger.error(mrc.getMessage("odin.security.autenticacion.usuario.inactivo.error", null, null));
					}
				} // Fin validación de usuario estado
			}
			else {
				messageResponse.setCode(HttpStatus.SC_OK);
				messageResponse.setMessage(
					mrc.getMessage("odin.security.autenticacion.usuario.resetPasswordExpire", null, null));
					logger.error(mrc.getMessage(
								 "odin.security.autenticacion.usuario.resetPasswordExpire", null, null));
			}
		}
		else {
			messageResponse.setMessage(mrc.getMessage("odin.security.autenticacion.usuario.inactivo.error", null, null));
			logger.error(mrc.getMessage("odin.security.autenticacion.usuario.inactivo.error", null, null));
			}// Fin usuarios existentes
		}
		catch (Exception e) {
			logger.error("Hubo un error inesperado al iniciar sesión:\n" + e.getLocalizedMessage());
			messageResponse.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			messageResponse.setMessage("Hubo un error inesperado al iniciar sesión:\n" + e.getLocalizedMessage());
			return messageResponse;
		}
		messageResponse.setCode(HttpStatus.SC_PARTIAL_CONTENT);
		return messageResponse;
	}

	@Override
	public Response logout(HttpServletRequest request) {
		Response messageResponse = new Response();

		String accessToken = request.getParameter(AppConstants.General.ACCESS_TOKEN_VALUE).toString();

		Session session = null;

		try {
			session = (Session) MasterSession.instance().getSessionTokenMap().get(accessToken);
			if(session == null) {
				messageResponse.setCode(org.apache.http.HttpStatus.SC_NOT_FOUND);
				messageResponse.setMessage("El token no existe, no hay usuario en sesión");
				return messageResponse;
			}
			
			logger.info(mrc.getMessage("odin.security.autenticacion.usuario.logout.validacion.success", null, null));

			MasterSession.instance().removeKey(accessToken, "sessionToken");
			MasterSession.instance().removeKey(session.getUsuario().getLogin(),"sessionCounter");
			MasterSession.instance().removeKey(session.getUsuario().getId()+"", "sessionUser");
			logger.info(mrc.getMessage("odin.security.autenticacion.usuario.logout.borrar.session.success", null, null));

			messageResponse.setMessage(mrc.getMessage("odin.security.autenticacion.usuario.logout.success", null, null));
			logger.info("User: " + session.getUsuario().getLogin() + " "
					+ mrc.getMessage("odin.security.autenticacion.usuario.logout.success", null, null));

			Usuario usuarioFound = userService.findOne(session.getUsuario().getId());
			usuarioFound.setUltimoLogin(new Date());
			userService.save(usuarioFound);

			AuditAuth auditAuth = auditAuthService.findById(session.getIdAudit());
			auditAuth.setFechaLogout(new Date());
			auditAuthService.save(auditAuth);
			messageResponse.setCode(HttpStatus.SC_OK);
		}
		catch(Exception e) {
			String errorMessage = "Excepción cerrando sesión".concat(e.getLocalizedMessage());
			logger.error(errorMessage);
			messageResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			messageResponse.setMessage(errorMessage);
		}
		return messageResponse;
	}
	
	private void auditFailLogin(AuthWrapper authwrapper, String motivo, String clientIp) {
		AuditAuthFallos auditAuthFallos = new AuditAuthFallos();
		auditAuthFallos.setDescripcion(motivo);
		auditAuthFallos.setFechaIntento(new Date());
		auditAuthFallos.setIdUsuario(userService.findByLogin(authwrapper.getUser()).getId().toString());
		auditAuthFallos.setIpOrigen(clientIp);
		auditAuthFallos.setLogin(authwrapper.getUser());
		auditAuthFallosService.save(auditAuthFallos);
		auditAuthFallos = null;
	}
	
	private void auditLogin(Session session) {
		AuditAuth auditAuth = new AuditAuth();
		auditAuth.setDescripcion(mrc.getMessage("odin.security.autenticacion.usuario.login.success", null, null));
		auditAuth.setFechaLogin(new Date());
		auditAuth.setIdUsuario(session.getUsuario().getNumeroIdentificacion());
		auditAuth.setIpOrigen(session.getIp());
		auditAuth.setLogin(session.getUsuario().getLogin());
		auditAuth.setTipoOperación("DML");
		auditAuthService.save(auditAuth);
		// Actualiza el Id de sesión de Auditoria para actualizarlo posteriormente en logout
		session.setIdAudit(auditAuth.getId());
		auditAuth = null;
	}
	
	private LoginResponse login(OAuthResponse token, Usuario usuario, String clientIp,
			MenuTemplate menu) {
		Session session = new Session();
		LoginResponse loginResponse = new LoginResponse();
		
		session.setDateTimeSession((Calendar.getInstance().getTime()));
		session.setToken(token);
		session.setUsuario(usuario);
		session.setIp(clientIp);
		
		MasterSession.instance().putEntry(token.getAccessToken(), session, "sessionTokenMap");
		MasterSession.instance().putEntry(usuario.getId()+"", session, "sessionUser");
		

		//loginResponse.setMenuOpciones(menu);
		loginResponse.setMenuTemplate(menu);
		loginResponse.setAccessToken(token.getAccessToken());
		loginResponse.setIP(clientIp);
		loginResponse.setCambioClave(usuario.isCambioClave());
		loginResponse.setUsuarioRolEntidad(usuario.getUsuarioRolEntidad());
		loginResponse.setLastLogin(usuario.getUltimoLogin());
		loginResponse.setUserFullName(usuario.getNombres() + " " + usuario.getApellidos());

		// Establece las rutas de acceso general para los uusarios
		loginResponse.setScope(usuario.getPerfil().getNombre());
		loginResponse.setId(usuario.getId());
		loginResponse.setLogin(usuario.getLogin());
		
		loginResponse.setUriGuardarClave(AppPaths.ODIN_GENERAL + AppPaths.ODIN_GENERAL_ACCESS_UPDATE_PASSWORD);
		loginResponse.setUriValidarClaveHistorico(
				AppPaths.ODIN_GENERAL + AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_PASSWORD_HISTORICO);
		loginResponse.setUriValidarClaveRestringido(
				AppPaths.ODIN_GENERAL + AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_PASSWORD_DICCIONARIO);
		loginResponse.setUriValidarUsuarioDocumentoEntidad(
				AppPaths.ODIN_GENERAL + AppPaths.GENERAL_ACCESS_VALIDATE_USER_ENTITY_ID_CONTEXT);
		loginResponse.setUriValidarPerfilEliminar(
				AppPaths.ODIN_GENERAL + AppPaths.VALIDATE_PROFILE_FOR_DELETE_CONTEXT);
		loginResponse.setUriLogout("/authenticate" + AppPaths.Security.ODIN_SECURITY_LOGOUT_PATH);
		loginResponse.setUriValidaToken(AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_TOKEN);
		//loginResponse.setUriOperaciones(AppPaths.Operacion.PATH);
		loginResponse.setUriValidarFecha(AppPaths.ODIN_GENERAL + AppPaths.ODIN_GENERAL_ACCESS_VALIDATE_DATE);

		// Establece la respuesta
		loginResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
		loginResponse.setCode(org.apache.http.HttpStatus.SC_OK);

		// Establece el perfil del usuario
		loginResponse.setPerfilId(usuario.getPerfil().getId());
		
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		
//		if (usuario.getUsuarioRolEntidad().getTipoRol().equalsIgnoreCase(TipoEntidadEnum.SUPER_ADMIN.name()))
//			otrasOpciones.setListaOperaciones(operacionService.findAllLite());
//		else {
//			/**
//			 * Caso contratio, filtra por entidad asociada y fechas de inicio y
//			 * fin de la operación
//			 */
//			otrasOpciones.setListaOperaciones(
//					operacionService.findAllLiteEntitiesNOSuperAdmin(usuario.getUsuarioRolEntidad()));
//		}

		loginResponse.setOtrasOpciones(otrasOpciones);
		auditLogin(session);
		
		return loginResponse;
	}

	private boolean activeUser(Usuario usuario) {
		return usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.ACTIVO.name());
	}

	private boolean validateAllowedAttempts(String user) {
		int quantityAttemps = AppConfiguration.intance().getConfiguracionOdinMap()
							  .get(AppConstants.General.ODIN_APPLICATION_CONFIGURATION)
							  .getCantidadIntentosPermitidos();
		if (MasterSession.instance().getSessionCounterMap().get(user) >= (quantityAttemps - 1))
			return false;
		return true;
	}

	private OAuthResponse getAccessToken(Usuario usuario) {
		String tokenServiceUrl = HermesMachine.getServiceURL("tokenservice", "gettoken", springEnvironment,
															 usuario.getLogin());
		logger.info("por preguntar por un nuevo token de acceso en: " + tokenServiceUrl);
		OAuthResponse oauthRes = (OAuthResponse) HermesMachine.consumeRestService(MediaType.APPLICATION_JSON,
																				  tokenServiceUrl, HttpMethod.GET,
																				  OAuthResponse.class, null).getBody();
		logger.info("respuesta OAuth: " + oauthRes);
		return oauthRes;
	}

	private boolean ipInWhiteList(String ipNeedle, List<IpPermitida> ipHaystack) {		
		return ipHaystack.stream().anyMatch(ip -> ip.getIp().trim().equals(ipNeedle));
	}

	private boolean validPasswordReset(Usuario usuario) {		
		String serviceUrl = HermesMachine.getServiceURL("prservice", "getPasswordReset", springEnvironment,
														usuario.getLogin());
		logger.info("averiguando si existe un reestablecimiento de contraseña en el servicio PR en: " + serviceUrl);
		
		try {
		/*DynamicLink din = (DynamicLink) HermesMachine.consumeRestService(MediaType.TEXT_PLAIN, serviceUrl, 
						    HttpMethod.GET, DynamicLink.class, null).getBody();*/
		DynamicLink din  = prService.findPasswordReset(usuario.getLogin());
		
		if(din != null) {
			if(!din.isUsed()) {
				if(din.getExpire_time().before(Calendar.getInstance().getTime()))//Expiró
				{
					String oldPassword = passwordHistorialOwnRepository.findByUsuario(usuario.getId());
					
					if(oldPassword != null){
						usuario.setPassword(oldPassword);
						userService.save(usuario);
						//cerberoSynhService.saveUser(usuario);
						prService.deletePasswordReset(usuario.getLogin());
						logger.info("Contraseña temporal para " + usuario.getLogin() +" expiró.");
						return false;
					}
					//usuario.setEstado(EstadosUsuarioEnum.BLOQUEADO.name());
					
				}
			}
//			String deleteDynServiceUrl = HermesMachine.getServiceURL("prservice", "deletePasswordReset",
//																	 springEnvironment, usuario.getLogin());
//			HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, deleteDynServiceUrl, 
//				    HttpMethod.DELETE, String.class, null).getBody();

				prService.deletePasswordReset(usuario.getLogin());
				logger.info("Eliminación de reestablecimiento de contraseña exitoso.");
			
				logger.info("Reestablecimiento de contraseña eliminado");
				return true;
		}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		logger.info("Sin reestablecimiento de contraseña");
		return true;
	}
	
	public String bloquearUsuario(UserService usuarioService, AuthWrapper authwrapper, String motivo)
			throws IOException {

		Response messageResponse = new Response();

		Usuario usuarioAttemp = usuarioService.findByLogin(authwrapper.getUser());

		// Bloquea ÚNICAMENTE aquellos usuarios en estado ACTIVO
		if (usuarioAttemp != null && usuarioAttemp.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.ACTIVO.name())) {

			usuarioAttemp.setEstado(EstadosUsuarioEnum.BLOQUEADO.name());
			
			String serviceUrl = HermesMachine.getServiceURL("tokenservice", "deleteuser", springEnvironment, 
															usuarioAttemp.getLogin());
			String tokenResponse = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrl,
																	HttpMethod.DELETE, String.class, null)
												.getBody().toString();
			logger.info("Token service response: " + tokenResponse);
			//tokenService.deleteUser(usuarioAttemp);
			usuarioService.save(usuarioAttemp);
			//cerberoSynhService.saveUser(usuarioAttemp);
			

			switch (motivo) {

			case AppConstants.General.BLOQUEO_INTENTOS_PERMITIDOS:
				logger.info("Usuario: " + authwrapper.getUser() + " "
						+ mrc.getMessage("odin.security.autenticacion.usuario.bloqueo.intentos.error", null, null));
				messageResponse.setMessage(
						mrc.getMessage("odin.security.autenticacion.usuario.bloqueo.intentos.error", null, null));
				break;
			case AppConstants.General.BLOQUEO_IP_PERMITIDA:
				logger.info("Usuario: " + authwrapper.getUser() + " "
						+ mrc.getMessage("odin.security.autenticacion.usuario.bloqueo.ip.motivo.error", null, null));
				messageResponse.setMessage(
						mrc.getMessage("odin.security.autenticacion.usuario.bloqueo.ip.motivo.error", null, null));
				break;
			default:
				logger.info("Usuario: " + authwrapper.getUser()
						+ mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
				messageResponse
						.setMessage(mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
				break;
			}

		}
		else
			messageResponse.setMessage(mrc.getMessage("odin.security.autenticacion.usuario.login.error", null, null));
		
		return messageResponse.getMessage();
	}
	
	
	/*
	 * Método para consultar el id del usuario en session por medio del accessToken
	 * */
	public Response getUserId(String accessToken){
		Response res = null;
		if(MasterSession.instance().containsKey(accessToken, "sessionMap")) {
			Integer userIdInSession = ((Session)MasterSession.instance().getValue(accessToken, "sessionMap"))
							        .getUsuario().getId();
			logger.info("Usuario con ID " + userIdInSession + " en sesión");
			return (new Response(HttpStatus.SC_OK, "Usuario con ID " + userIdInSession + " en sesión",
										 accessToken, userIdInSession+""));
		}
		else
			res = new Response(HttpStatus.SC_NOT_FOUND, "No existe un usuario en sesión con el token indicado ",
							   accessToken, NO_USER_IN_SESSION_CONSTANT);
		
		logger.info(res.getMessage());
		return res;
		
		
	}
	
	public ResourceResponse validateUserEntity( UsuarioEntidadWrapper wrapper , Integer caso ){
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		
		if (wrapper != null
				&& (wrapper.getNumeroDocumento() != null && wrapper
						.getNumeroDocumento().length() > 0)
				&& (wrapper.getUsuarioRolEntidad() != null
						&& wrapper.getUsuarioRolEntidad().getEntidad()
								.getId() != null && !wrapper
						.getUsuarioRolEntidad().getTipoRol()
						.equalsIgnoreCase(""))
				&& (wrapper.getTipoDocumento() != null && wrapper
						.getTipoDocumento().getId() != null)) {

			logger.info(wrapper);

			List<Usuario> usersFound = userService
					.findByIdentificacionAndTipoDocumentoAndEntidadAndRol(
							wrapper.getNumeroDocumento(), wrapper
									.getTipoDocumento(), wrapper
									.getUsuarioRolEntidad().getEntidad(),
							wrapper.getUsuarioRolEntidad().getTipoRol());

			int usersListSize = usersFound.size();

			/**
			 * Si el id viene NO nulo, quiere decir que la validación se
			 * está haciendo desde una actualización de Usuario y debe
			 * excluirse la validación con el id entrante (el existente es
			 * el mismo usuario)
			 */
			boolean sameUser = false;

			if (null != wrapper.getId()) {

				Usuario usuario = userService.findOne(wrapper.getId());

				if (usuario.getId().intValue() == wrapper.getId())
					sameUser = true;
			}
			

			/*
			 * Si ya existe al menos 1 usuario pero NO es el mismo que se
			 * está modificando, responde OK para evitar que se creen 2
			 * usuarios con el mismo tipo de documento, el mismo número de
			 * documento y el mismo Rol
			 */
			logger.info("usersListSize" + usersListSize);
			if (usersListSize > 0 && !sameUser) {
				
				if(usersListSize == 1){
					logger.info("Entro por uno");
					otrasOpciones.setBusinessErrorCode(AppConstants.ODIN_USER_ROL_ENTIDAD_ONE);
					resourceResponse.setOtrasOpciones(otrasOpciones);
				}
				
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
				resourceResponse.setMessage(mrc.getMessage(
										"odin.security.autorizacion.resourceAccess.OPTIONS.success",
										null, null));
				logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OPTIONS.success",
								null, null));

				return  resourceResponse;
			}
			else {
				/*
				 * No existen usuarios, realiza consulta por: numero
				 * documento, documento y entidad
				 */
				List<Usuario> usuariosExistentes = userService
						.findByIdentificacionAndTipoDocumentoAndEntidad(
								wrapper.getNumeroDocumento(), wrapper
										.getTipoDocumento(), wrapper
										.getUsuarioRolEntidad()
										.getEntidad());

				// Entro a validar cuando aún puede insertarse un registro
				// más
				logger.info("usuariosExistentes.size() " + usuariosExistentes.size() + usuariosExistentes.toString());
				if (usuariosExistentes.size() < 2) {
					// Si no existe absolutamente ningún usuario, lo deja
					// crear sin ningpun problema
					if (usuariosExistentes.size() == 0) {
						// Deja agregar el usuario
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
						return resourceResponse;

					} 
					else {
						Usuario existentUser = usuariosExistentes.get(0);

						/**
						 * Única y exlusivamente deja insertar un nuevo
						 * usuario con la sigueinte combinación:
						 * http://jira.bvc.com.co:8080/browse/SEE-50
						 * 
						 * 1. Un login con mismo documento en entidad A tipo
						 * SCB + Un login con mismo documento en entidad A
						 * tipo Emisora
						 * 
						 * 
						 * 2. Un login con mismo documento en entidad A tipo
						 * Afiliado al MEC + Un login con mismo documento en
						 * entidad A tipo Emisora
						 * 
						 * 3. Un login con mismo documento en entidad A tipo
						 * Super administradora + Un login con mismo
						 * documento en entidad A tipo Emisora
						 * 
						 * 
						 */

						if (existentUser.getUsuarioRolEntidad().getTipoRol() 
								.equals(TipoEntidadEnum.EMISOR.name())
								&& (wrapper.getUsuarioRolEntidad()
										.getTipoRol()
										.equals(TipoEntidadEnum.SCB.name())
										|| wrapper
												.getUsuarioRolEntidad()
												.getTipoRol()
												.equals(TipoEntidadEnum.AFILIADO
														.name()) || wrapper
										.getUsuarioRolEntidad()
										.getTipoRol()
										.equals(TipoEntidadEnum.SUPER_ADMIN
												.name()))) {


							/*
							 * Se envia 1 cuando es consulta al guardar 
							 * 0 cuando es consulta antes de guardar desde front*/
							
							if(caso == 0){
								otrasOpciones.setBusinessErrorCode(AppConstants.ODIN_USER_ROL_ENTIDAD_TWO);
								resourceResponse.setOtrasOpciones(otrasOpciones);
								resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
								
							}else{
								// Deja agregar el usuario
								resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
							}
													

							return resourceResponse;

						} else if ((existentUser.getUsuarioRolEntidad()
								.getTipoRol()
								.equals(TipoEntidadEnum.SCB.name())
								|| existentUser
										.getUsuarioRolEntidad()
										.getTipoRol()
										.equals(TipoEntidadEnum.AFILIADO
												.name())
								|| existentUser
										.getUsuarioRolEntidad()
										.getTipoRol()
										.equals(TipoEntidadEnum.SUPER_ADMIN
												.name()))
								&& (wrapper.getUsuarioRolEntidad()
										.getTipoRol()
										.equals(TipoEntidadEnum.EMISOR
												.name()))) {

							/*
							 * Se envia 1 cuando es consulta al guardar 
							 * 0 cuando es consulta antes de guardar desde front*/
							
							if(caso == 0){
								otrasOpciones.setBusinessErrorCode(AppConstants.ODIN_USER_ROL_ENTIDAD_TWO);
								resourceResponse.setOtrasOpciones(otrasOpciones);
								resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
								
							}else{
								// Deja agregar el usuario
								resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
							}
							return resourceResponse;
						}
					}
				}
				else {
					// Ya existen usuarios....
					
												
						otrasOpciones.setBusinessErrorCode(AppConstants.ODIN_USER_ROL_ENTIDAD_ONE);
						resourceResponse.setOtrasOpciones(otrasOpciones);
						
					
					
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
					return resourceResponse;
				}

				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
				resourceResponse.setMessage(mrc.getMessage(
										"odin.security.autorizacion.resourceAccess.POST.success",
										null, null));
			
				logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.POST.success",
								null, null));
			}
		}
		else {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
			logger.info(mrc.getMessage("odin.recurso.params.error", null,null));
			return resourceResponse;
		}
		return resourceResponse;
	}
}