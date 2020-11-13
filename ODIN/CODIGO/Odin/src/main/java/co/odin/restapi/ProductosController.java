package co.odin.restapi;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.common.config.MessageSourceConfig;
import co.common.entities.Perfil;
import co.common.entities.Usuario;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import co.odin.exception.UsuariosException;
import co.odin.security.MasterSession;
import co.odin.security.Session;
import co.odin.services.MenuTemplateService;
import co.odin.services.ProfileService;
import co.odin.services.UserService;
import co.odin.utils.AppConstants;
import co.odin.utils.AppPaths;

@RestController
@RequestMapping(AppPaths.Productos.PRODUCTOS)
@Api(value = "Procesos controller")
public class ProductosController {
	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(ProductosController.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private MessageSource mrc = MessageSourceConfig.messageSource();
	// ***********************************
	// Inyección de dependencias
	//
	@Autowired
	private UserService userService;

	@Autowired
	private MenuTemplateService menuTemplateService;

	@Autowired
	private ProfileService profileService;

	@ApiOperation(value = AppPaths.Productos.PRODUCTOS, nickname = "procesos", notes = "trae los permisos para procesos de carrito")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> get(HttpServletRequest request,
			@RequestParam(value = "nombre", required = false) String nombre) throws UsuariosException {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Inicializar las variables como NO-CONTENT
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse
					.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.NOT_FOUND", null, null));

			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			//resourceResponse.setResource(AppPaths.ProcesosRedepartes.PROCESOS_CLIENTES);
			System.out.println("Pasa por el controlador de cliente en cerbero.." + nombre);

			//otrasOpciones.setClientes(userService.cargaClientes(nombre));

			// Consulta la sesión en el Map por accessToken como llave
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

			// Debo consultar en base de datos por id de Usuario:
			// Se hace asi porque el usuarios está sujeto a actualizaciones
			Usuario usuario = userService.findOne(session.getUsuario().getId());

			// Trae la información de perfil
			Perfil perfil = profileService.findOne(usuario.getPerfil().getId());

			// ****************************************************************
			// Retorna los permisos que tiene sobre la administración de
			// usuarios

			System.out.println("Pasa por el controlador de clientes en cerbero");
			otrasOpciones.setPermisos(menuTemplateService.getPermisosByPerfil(perfil, AppPaths.Productos.PRODUCTO));
			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));
			logger.info(mrc.getMessage("odin.security.autorizacion.resourceAccess.OK", null, null));

		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("odin.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
}
