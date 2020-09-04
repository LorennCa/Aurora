package co.ias.usuarios.restapi.interceptors;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import co.ias.common.entities.IpPermitida;
import co.ias.common.response.Response;
import co.ias.common.wrappers.ResourceAccessWrapper;
import co.ias.usuarios.config.appconfig.AppConfiguration;
import co.ias.usuarios.config.appconfig.MessageSourceConfig;
import co.ias.usuarios.security.MasterSession;
import co.ias.usuarios.security.Session;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.AppPaths;
import co.ias.usuarios.utils.UsuariosUtil;
import co.ias.usuarios.utils.HermesMachine;

@Component
public class ApiCallInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Logger de apache log4j
	 */
	private static final Logger logger = LogManager.getLogger(ApiCallInterceptor.class);

	/**
	 * MessageResource de acceso a las fuentes de mensajes del sistema
	 */
	private static final MessageSource mrc = MessageSourceConfig.messageSource();

	//@Autowired
	//private AuthorizationService authorizationService;

	/**
	 * Realiza el proceso de Validación de autorización para cada petición de
	 * acceso a Recurso
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// Si es autorizado, se cambia a true
		boolean authorizationResult = false;

		Response messageReponse = new Response();

		try {

			String ip = UsuariosUtil.getIPFromRequest(request);
			String scope = request.getParameter(AppConstants.General.AUTORIZACION_SCOPE);
			String accessToken = request.getParameter(AppConstants.General.ACCESS_TOKEN_VALUE);
			String URI = request.getParameter(AppConstants.General.AUTORIZACION_URI);

			request.setAttribute(AppConstants.General.AUTORIZACION_URI, URI);
			request.setAttribute(AppConstants.General.AUTORIZACION_IP, ip);

			logger.info("Imprimiendo los params scope: {} , accessToken: {} , URI {} ", scope, accessToken, URI);
			logger.info("Accediendo desde método: {}", request.getMethod());

			ResourceAccessWrapper raw = new ResourceAccessWrapper();
			raw.setAccessToken(accessToken);
			raw.setScope(scope);
			raw.setIp(ip);
			raw.setURI(URI);

			if (null != raw.getScope() && !raw.getScope().isEmpty()
					&& null != raw.getAccessToken()
					&& !raw.getAccessToken().isEmpty() && null != raw.getURI()
					&& !raw.getURI().isEmpty()) {

				// Realiza el proceso de validación
				messageReponse = validateResourceAccess(raw);

				if (messageReponse.getCode() == org.apache.http.HttpStatus.SC_OK) {
					/**
					 * Al validarse todo OK, valida por la expiración del token:
					 * 1. Expirado? Se renueva a través de el meca RefreshToken.
					 * 2. Vigente? Contúa enviándose el mismo
					 */
					Session session = MasterSession.instance().getSessionTokenMap().get(raw.getAccessToken());

					// Envía al controlador el Token de acceso (actual o renovado)
//					request.setAttribute(AppConstants.General.ACCESS_TOKEN_VALUE,
//							session.getToken().getAccessToken());
					request.setAttribute(AppConstants.General.ACCESS_TOKEN_VALUE,
							session.getToken().getAccessToken());
					authorizationResult = true;

				} else {
					response.setStatus(messageReponse.getCode());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.getWriter().write((HermesMachine.convertToJson(messageReponse)));
				}

			} else {
				messageReponse
						.setCode(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
				messageReponse.setMessage(mrc.getMessage(
						"ias.security.autorizacion.resourceAccess.error", null,
						null));
				logger.info(mrc.getMessage(
						"ias.security.autorizacion.resourceAccess.error", null,
						null));
				response.setStatus(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(HermesMachine.convertToJson(messageReponse));
			}
		} catch (Exception e) {
			new Exception(e);
			logger.info(
					mrc.getMessage(
							"ias.security.autorizacion.resourceAccess.interceptor.error",
							null, null), e.getMessage());
			response.setStatus(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			messageReponse.setMessage(e.getMessage());
			response.getWriter().write(messageReponse.toString());
			return authorizationResult;
		}
		return authorizationResult;
	//return true;
	}

	private Response validateResourceAccess(ResourceAccessWrapper raw) throws Exception {
		Response mr = new Response();
		try {

			/**
			 * Se inicializa con un código de acceso prohibido. En caso que pase la validación, 
			 * se actualizará el código por un OK (200)
			 */
			mr.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);

			// Existen todos los parámetros de autorización
			if (raw != null && (!raw.getAccessToken().isEmpty() || raw.getAccessToken() != null)
					&& (!raw.getScope().isEmpty() || raw .getScope() != null)
					&& (!raw.getURI().isEmpty() || raw.getURI() != null))
			{				
				if (!MasterSession.instance().containsKey(raw.getAccessToken(), "sessionMap")) {
					mr.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.accessToken.error",null, null));
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.accessToken.error",null, null));
					return mr;
				}
				boolean validateActiveIP = AppConfiguration.intance().getSeeConfig().isValidarIP();
				Session session = (Session) MasterSession.instance().getSessionTokenMap().get(raw.getAccessToken());
				
				/** Valida que la IP donde se origina la solicitud, esté en el listado de IPs permitidas **/
				if (validateActiveIP) {					
					if (!validateIP(raw.getIp(), session.getUsuario().getListIpPermitidas())) {
						// Actualiza el mensaje de respuesta a la vista:
						mr.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.ip.error",
										null, null));
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.ip.error",
										null, null));
						return mr;
					}
				}
				
				/** Valida que el scope (perfil) sea válido para el usuario que hizo el proceso de 
				 * autenticación **/
				if (!session.getUsuario().getPerfil().getNombre().equals(raw.getScope())) {
					mr.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.scope.error",
									null, null));
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.scope.error",
									null, null));
					return mr;
				}
				
				/** Valida que el cliente tenga acceso a la URI que se solicita **/
				if (!validateURIAccess(raw.getURI(), session)) {
					mr.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.URI.error",
												 null, null));
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.URI.error",
											   null, null));
					return mr;
				}
			}
			else {
				mr.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.error", null,null));
				logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.error", null, null));
				return mr;
			}
		}
		catch(Exception e) {
			logger.error(mrc.getMessage("ias.security.autorizacion.resourceAccess.exception", null,
					null), e.getMessage());
			throw new Exception(e);
		}
		mr.setCode(org.apache.http.HttpStatus.SC_OK);
		return mr;
	}

	private boolean validateURIAccess(String uriResource, Session session) {
		/** Excluye de la validación el acceso general de usuarios **/
		if (uriResource.trim().contains(AppPaths.IAS_GENERAL) || uriResource.trim().contains("/authenticate"))
			return true;
		
		return session.getUsuario().getPerfil().getOpcionesMenu().stream()
					  .filter(menu -> menu.getUriServicio() != null)
					  .anyMatch(menu -> menu.getUriServicio().trim().toLowerCase()
							    		.equals(uriResource.toLowerCase()));
	}

	private boolean validateIP(String ipNeedle, List<IpPermitida> ipHaystack) {
		return ipHaystack.stream().anyMatch(ip -> ip.getIp().trim().equals(ipNeedle));
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
	}
}