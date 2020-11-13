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

import org.springframework.web.bind.annotation.RestController;

import co.aurora.config.appconfig.MessageSourceConfig;
import co.aurora.services.InventarioService;
import co.aurora.utils.AppConstants;
import co.aurora.utils.AppPaths;
import co.common.entities.Inventario;
import co.common.response.OtrasOpciones;
import co.common.response.ResourceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(AppPaths.INVENTARIO)
@Api(value = "Aurora principal controller")
public class InventarioController {
	
	private MessageSource mrc = MessageSourceConfig.messageSource();
	
	@Autowired
	private InventarioService inventarioService;
	
	private static final Logger logger = LogManager.getLogger(InventarioController.class);
	
	
	@ApiOperation(value = AppPaths.MOSTRAR_INVENTARIO, nickname = "procesos", notes = "MOstrar inventario")
	@RequestMapping(value = AppPaths.MOSTRAR_INVENTARIO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> consultaReferencias(HttpServletRequest request) throws SQLException {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		logger.info("Inicia consulta de inventario...");
		try {
			// envia nuevamente el accessToken y uri
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();

			resourceResponse.setAccessToken(accessToken);
			otrasOpciones.setInventario(inventarioService.findAll());
			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("aurora.security.autorizacion.resourceAccess.OK", null, null));
			logger.info("Finaliza consulta de inventario...");
		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("aurora.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}
	
	
	@ApiOperation(value = AppPaths.CREAR_PRODUCTO, nickname = "procesos", notes = "Creación de producto")
	@RequestMapping(value = AppPaths.CREAR_PRODUCTO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> crearProducto(@RequestBody Inventario producto, HttpServletRequest request) {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		try {
			// Envía nuevamente el accessToken y el recurso
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();
			logger.info("Inicia creacion de proveedor");
			logger.info(producto.getClave().getId_producto());
			logger.info(producto.getClave().getReferencia());
			logger.info(producto.getClave().getColor());
			logger.info(producto.getCantidad());
			logger.info(producto.getRecursoUno());
			logger.info(producto.getPalabra_clave());
			resourceResponse.setAccessToken(accessToken);
			inventarioService.crearProducto(producto);

			logger.info(mrc.getMessage("aurora.security.autorizacion.resourceAccess.POST.success", null, null));
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse
					.setMessage(mrc.getMessage("aurora.security.autorizacion.resourceAccess.POST.success", null, null));
			resourceResponse.setOtrasOpciones(otrasOpciones);
			logger.info("Finaliza creacion de proveedor");
		} catch (Exception e) {
			logger.error(mrc.getMessage("aurora.recurso.POST.ERROR", null, null), "Usuario", "", e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("aurora.recurso.POST.ERROR", null, null), "Usuario", "", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	@ApiOperation(value = AppPaths.ACTUALIZAR_PRODUCTO, nickname = "procesos", notes = "Actualizar producto")
	@RequestMapping(value = AppPaths.ACTUALIZAR_PRODUCTO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> actualizarProducto(@RequestBody Inventario producto, HttpServletRequest request) {
		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		try {
			// Envía nuevamente el accessToken y el recurso
			String accessToken = request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE) == null
					? request.getParameter(AppConstants.ACCESS_TOKEN_VALUE)
					: request.getAttribute(AppConstants.ACCESS_TOKEN_VALUE).toString();
			logger.info("Inicia actualización de producto");
			logger.info(producto.getClave().getId_producto());
			logger.info(producto.getClave().getReferencia());
			logger.info(producto.getClave().getColor());
			logger.info(producto.getCantidad());
			logger.info(producto.getRecursoUno());
			logger.info(producto.getPalabra_clave());
			resourceResponse.setAccessToken(accessToken);
			inventarioService.actualizarProducto(producto);

			logger.info(mrc.getMessage("aurora.security.autorizacion.resourceAccess.POST.success", null, null));
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse
					.setMessage(mrc.getMessage("aurora.security.autorizacion.resourceAccess.POST.success", null, null));
			resourceResponse.setOtrasOpciones(otrasOpciones);
			logger.info("Finaliza creacion de proveedor");
		} catch (Exception e) {
			logger.error(mrc.getMessage("aurora.recurso.POST.ERROR", null, null), "Usuario", "", e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("aurora.recurso.POST.ERROR", null, null), "Usuario", "", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}


}
