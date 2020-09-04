package co.ias.usuarios.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import co.ias.common.response.Response;
import co.ias.usuarios.config.appconfig.MessageSourceConfig;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.HermesMachine;

@Component
public class IASApiCallInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Logger de apache log4j
	 */
	private static final Logger logger = LogManager.getLogger(IASApiCallInterceptor.class);

	/**
	 * MessageResource de acceso a las fuentes de mensajes del sistema
	 */
	private static final MessageSource mrc = MessageSourceConfig.messageSource();

	//@Autowired
	//private AuthorizationService authorizationService;

	/**
	 * Procesa cada solicitud en busca de 3 parámetros string no vacíos.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// Si es autorizado, se cambia a true
		boolean authorizationResult = false;

		Response messageReponse = new Response();

		try {

			String ip = HermesMachine.getIPFromRequest(request);
			String scope = request.getParameter(AppConstants.AUTHORIZATION_SCOPE);
			String accessToken = request.getParameter(AppConstants.AUTHORIZATION_ACCESS_TOKEN);
			String uri = request.getParameter(AppConstants.AUTHORIZATION_URI);


			logger.info("parámetros de autorización = scope: {} , accessToken: {} , URI {} ", 
						scope, accessToken, uri);
			logger.info("Método usado: {}", request.getMethod());
			logger.info("Accediendo desde: {}", ip);

			
			if (scope !=null && !scope.isEmpty() && accessToken != null && !accessToken.isEmpty() 
				&& uri != null && !uri.isEmpty()) {
				authorizationResult = true;
			}
			else {
				messageReponse.setCode(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
				messageReponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.error", null,
														 null));
				logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.error", null, null));
				response.setStatus(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(HermesMachine.convertToJsonGW(messageReponse));
			}
		} catch (Exception e) {
			new Exception(e);
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.interceptor.error", null, null),
									   e.getMessage());
			response.setStatus(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			messageReponse.setMessage(e.getLocalizedMessage());
			response.getWriter().write(HermesMachine.convertToJsonGW(messageReponse.toString()));
			return authorizationResult;
		}
		return authorizationResult;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
	}
}