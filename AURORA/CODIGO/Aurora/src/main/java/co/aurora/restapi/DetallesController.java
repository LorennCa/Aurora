package co.aurora.restapi;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.aurora.config.appconfig.MessageSourceConfig;
import co.aurora.repositories.CarritoRepository;
import co.aurora.services.CarritoService;
import co.aurora.services.DetallesService;
import co.aurora.services.InventarioService;
import co.aurora.utils.AppConstants;
import co.aurora.utils.AppPaths;
import co.common.entities.Carrito;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(AppPaths.DETALLES)
@Api(value = "Aurora principal controller")
public class DetallesController {

	private MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private DetallesService detalleService;
	
	@Autowired
	private CarritoRepository carritoRepository;
	
	@Autowired
	private CarritoService carritoService;
	
	private static final Logger logger = LogManager.getLogger(DetallesController.class);
	
	
	@ApiOperation(value = AppPaths.CONSULTAR_DETALLES, nickname = "procesos", notes = "Mostrar detalles accesorio")
	@RequestMapping(value = AppPaths.CONSULTAR_DETALLES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> mostrarDetalles(HttpServletRequest request, @RequestParam(value = "nombre", required = false) String accesorio) throws SQLException {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		logger.info("Inicia consulta de detalles..."+accesorio);
		try {
			// envia nuevamente el accessToken y uri
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();

			resourceResponse.setAccessToken(accessToken);
			otrasOpciones.setDetalles(detalleService.retornaDetalles(accesorio));
			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("aurora.security.autorizacion.resourceAccess.OK", null, null));
			logger.info("Finaliza consulta de detalles...");
		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("aurora.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	
}
