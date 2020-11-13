package co.odin.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.common.entities.DiccionarioClaves;
import co.common.entities.Usuario;
import co.common.entities.UsuarioPasswordHistorial;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import co.common.response.Response;
import co.common.response.UsuarioLight;
import co.common.wrappers.ClaveWrapper;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.services.CerberoService;
import co.odin.services.PasswordDictionaryService;
import co.odin.services.UserService;
import co.odin.utils.AppPaths;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(AppPaths.Dictionary.SERVICE_GENERAL_PATH)
@Api(value = "Service principal controller")
public class PDServiceController {

	private static final Logger logger = LogManager.getLogger(PDServiceController.class);
	
	private MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private PasswordDictionaryService pdservice;
		
	@Autowired
	private UserService usuarioService;
	
	@Autowired
	private CerberoService cerberoService;
	
	@ApiOperation(value = AppPaths.Dictionary.SERVICE_GENERAL_PATH, nickname = "Diccionario", notes = "Valida si la palabra existe o no en el listado de las restringidas")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateClave(HttpServletRequest request, @RequestBody ClaveWrapper clave) throws Exception {
			/*@RequestParam(name="clave") String clave) throws EEException {*/
			
		ResourceResponse resourceResponse = new ResourceResponse();

		try {

			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getParameter(AppPaths.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Dictionary.ODIN_USER_PASSWORD_COMPLETE_CONTEXT);

			// Si viene la clave a valildar NO null
			if (null != clave.getClave() && !clave.getClave().isEmpty()
					&& clave.getClave().length() > 0) {

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

			} else {
				resourceResponse.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
				logger.info(mrc.getMessage("odin.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		}
		catch (Exception e) {
			logger.error(mrc.getMessage("odin.recurso.PUT.ERROR", null, null),
										"Diccionario Claves", " ", " ", e.getMessage());
			resourceResponse.setMessage(mrc.getMessage("odin.recurso.PUT.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.PUT.success", null, null));
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}	
	
	@ApiOperation(value = AppPaths.Dictionary.PASSWORD_DICTIONARY_CONTEXT, nickname = "Diccionario", notes = "Valida si la palabra existe o no en el listado de las restringidas")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@RequestMapping(value = (AppPaths.Dictionary.PASSWORD_DICTIONARY_CONTEXT), method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validatePassword(HttpServletRequest request, @RequestBody ClaveWrapper pass) {
		Response response = new Response();
		
		try {
			response.setAccessToken(request.getAttribute("accesstoken").toString());
			response.setResource(AppPaths.Dictionary.SERVICE_GENERAL_PATH);

			// Si viene la clave por valildar NO null
			if (null != pass.getClave() && !pass.getClave().isEmpty()) {
				// Si la clave a validar existe en las palabras restringidas, OK

				if (pdservice.inRestrictedWords(pass.getClave())) {
					response.setCode(org.apache.http.HttpStatus.SC_OK);
					response.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
					logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
				}
				else {
					response.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
					response.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND",
															   null, null));
					logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
				}
			}
			else {
				response.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				response.setMessage(mrc.getMessage("odin.recurso.params.error", null, null));
				logger.info(mrc.getMessage("odin.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		}
		catch(Exception e) {
			logger.error(mrc.getMessage("odin.recurso.PUT.ERROR", null, null),
					"Diccionario Claves", " ", " ", e.getMessage());
			response.setMessage(mrc.getMessage("odin.recurso.PUT.ERROR", null, null));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation(value = AppPaths.Dictionary.ODIN_DICCIONARIO_CLAVES, nickname = "Diccionario", notes = "Trae las palabras restringidas para claves")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> get(HttpServletRequest request) throws Exception {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {
			// Inicializar las variables como NO-CONTENT
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null,
													   null));
			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getParameter("accesstoken").toString());
			resourceResponse.setResource(AppPaths.Dictionary.ODIN_DICCIONARIO_CLAVES);

			// Retorna un único registro
			DiccionarioClaves diccionario = pdservice.findAll().get(0);

			if (diccionario != null) {
				// Set the Response
				otrasOpciones.setDiccionario(diccionario.getCadenaClaves());
				resourceResponse.setOtrasOpciones(otrasOpciones);
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
				resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null,null));
				logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null,null));
				return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
			}

		} catch (Exception e) {
			String errorMessage = mrc.getMessage("pdservice.GET.generic.error", null,null);
			logger.error(errorMessage, e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(errorMessage+"\n"+e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
		}
		logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null,null));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
	}
	
	@ApiOperation(value = AppPaths.Dictionary.ODIN_DICCIONARIO_CLAVES, nickname = "Diccionario", notes = "Actualiza la cadena de palabras restringidas para claves")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(HttpServletRequest request, @RequestBody ClaveWrapper cadenaClaves)
			throws Exception {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {
			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute("accesstoken").toString());
			resourceResponse.setResource(AppPaths.Dictionary.ODIN_DICCIONARIO_CLAVES);
			
			
			/**
			 * Debo consultar en base de datos por id de Usuario: Se
			 * hace asi porque el usuarios está sujeto a actualizaciones
			 */
			
			Response res = cerberoService.getUserId(resourceResponse.getAccessToken());
			Usuario usuario = usuarioService.findOne(Integer.parseInt(res.getResource())  );	

			// Retorna un único registro
			DiccionarioClaves diccionario = pdservice.findAll().get(0);

			if (null != diccionario) {
				// Set the Response
				logger.info(mrc.getMessage("odin.recurso.PUT", null, null), "Diccionario de Claves", null, null);

				// Establece la cadena de palabras reservadas
				diccionario.setCadenaClaves(cadenaClaves.getClave());

				// Actualiza la cadena de palabras reservadas
				pdservice.save(diccionario);

				// Establece response
				otrasOpciones.setDiccionario(diccionario.getCadenaClaves());
				
				/*
				 * Envia todos los usuarios de acuerdo al tipo de entidad al que este atado el usuarios
				 */
				
				List<Usuario> usuariosList = new ArrayList<Usuario>();

				
				if (usuarioService.esUsuarioEntidadSuperAdmin(usuario)
						|| usuarioService
								.esUsuarioPerfilSuperAdministrador(usuario)) {
					// Envía toda la info
					usuariosList = usuarioService.findAll();
					
				} else {
					usuariosList = usuarioService.findByEntidad(usuario.getUsuarioRolEntidad().getEntidad().getId());
				}
				
				for(Usuario user : usuariosList){
					user.setPassword("");
					user.setHistorialPassword(new ArrayList<UsuarioPasswordHistorial>());
				}

				//Response para UsuarioLigth
				List<UsuarioLight> usuarioResumen = usuarioService.parseUsuarioToUsuarioLight(usuariosList);
				
				otrasOpciones.setListaUsuarios(usuarioResumen);
				
				//otrasOpciones.setListaUsuarios(usuariosList);
					
				resourceResponse.setOtrasOpciones(otrasOpciones);
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
				resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.PUT.success",
															null, null));
			}
		}
		catch (Exception e) {
			String errorMessage = mrc.getMessage("odin.recurso.PUT.ERROR", null,null);
			logger.error(errorMessage, e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(errorMessage+"\n"+e.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
		}
		logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.PUT.success", null, null));
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
}