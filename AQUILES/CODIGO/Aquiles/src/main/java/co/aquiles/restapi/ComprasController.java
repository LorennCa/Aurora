package co.aquiles.restapi;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

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

import co.aquiles.config.appconfig.MessageSourceConfig;
import co.aquiles.services.ComprasService;
import co.aquiles.utils.AppConstants;
import co.aquiles.utils.AppPaths;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(AppPaths.COMPRAS)
@Api(value = "Aurora principal controller")
public class ComprasController {
private MessageSource mrc = MessageSourceConfig.messageSource();
	
	private static final Logger logger = LogManager.getLogger(ComprasController.class);
	
	@Autowired
	private ComprasService compraService;
	
	@ApiOperation(value = AppPaths.COMPRA, nickname = "procesos", notes = "Consultar proveedores")
	@RequestMapping(value = AppPaths.COMPRA, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> consultaCompras(HttpServletRequest request, @RequestParam(value = "nombre", required = false) String referencia) throws SQLException {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		logger.info("Inicia consulta de compras..."+referencia);
		try {
			// envia nuevamente el accessToken y uri
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();

			resourceResponse.setAccessToken(accessToken);
			otrasOpciones.setCompras(compraService.retornaCompras());
			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("aurora.security.autorizacion.resourceAccess.OK", null, null));
			logger.info("Finaliza consulta de compras...");
		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("aurora.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
}
