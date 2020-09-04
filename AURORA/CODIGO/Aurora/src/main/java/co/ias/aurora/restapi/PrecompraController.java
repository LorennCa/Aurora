package co.ias.aurora.restapi;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.ias.aurora.config.appconfig.MessageSourceConfig;
import co.ias.aurora.repositories.CarritoRepository;
import co.ias.aurora.services.CarritoService;
import co.ias.aurora.services.InventarioService;
import co.ias.aurora.utils.AppConstants;
import co.ias.aurora.utils.AppPaths;
import co.ias.common.entities.Carrito;
import co.ias.common.response.OtrasOpciones;
import co.ias.common.response.ResourceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(AppPaths.PRECOMPRA)
@Api(value = "IAS principal controller")
public class PrecompraController {


	private MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private InventarioService inventarioService;
	
	@Autowired
	private CarritoRepository carritoRepository;
	
	@Autowired
	private CarritoService carritoService;
	
	private static final Logger logger = LogManager.getLogger(CarritoController.class);
	@ApiOperation(value = AppPaths.CONSULTAR_CARRITO, nickname = "procesos", notes = "MOstrar inventario")
	@RequestMapping(value = AppPaths.CONSULTAR_CARRITO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> consultaCarrito(HttpServletRequest request) throws SQLException {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		logger.info("Inicia consulta de carrito...");
		try {
			// envia nuevamente el accessToken y uri
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();

			resourceResponse.setAccessToken(accessToken);
			otrasOpciones.setCarrito(carritoService.retornaCarrito(""));
			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
			logger.info("Finaliza consulta de inventario...");
		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("ias.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
}
