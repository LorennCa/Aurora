package co.ias.heracles.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ias.common.response.AppConfResponse;
import co.ias.heracles.config.ConfigurationIAS;
import co.ias.heracles.config.appconfig.AppInitParams;
import co.ias.heracles.entities.ConfiguracionIAS;
import co.ias.heracles.utils.AppConstants;



@RestController
@RequestMapping(AppConstants.SERVICE_ROOT_CONTEXT)
@Api(tags = {"Controlador principal REST para el servicio"})
public class ConfigController {
	
	private static final Logger logger = LogManager.getLogger(ConfigController.class);
	
	@ApiOperation(value = AppConstants.CONFIG_PATH, notes = "Averigua la configuración de seguridad")
	@ApiResponses(value = {
			@ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = "<cadena toString del objeto de respuesta>",
						 response = ConfiguracionIAS.class),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, 
						 message = "Error de aplicación inesperado", response = Exception.class)})
	@GetMapping(value = AppConstants.CONFIG_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSEEConfig(){
		logger.info("Cliente solicitando la configuración de parametrización");
		return ResponseEntity.status(HttpStatus.OK).body(ConfigurationIAS.instance().getConfig());
	}
	
	@ApiOperation(value = AppConstants.SEE_APP_CONF_PATH, notes = "Aaverigua la configuración inicial de la aplicación")
	@ApiResponses(value = {
			@ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = "Success", response = AppConfResponse.class),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, 
						 message = "Error de aplicación inesperado", response = Exception.class) })
	@GetMapping(value = { AppConstants.SEE_APP_CONF_PATH }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getApplicationConfiguration() {
		logger.info("Cliente solicitando configuración de aplicación");
		AppConfResponse appConfResponse = new AppConfResponse();
		appConfResponse.setCaptchaActivado(Boolean.parseBoolean(AppInitParams.instance().getCaptcha()));
		appConfResponse.setLogoPrincipal((AppInitParams.instance().getBannerPicture()));
		appConfResponse.setIntentosPermitidosLogin(AppInitParams.instance().getIntentosPermitidosLogin());
		appConfResponse.setTiempoSessionActiva(AppInitParams.instance().getTiempoSessionActiva());
		return ResponseEntity.status(HttpStatus.OK).body(appConfResponse);
	}
}