package co.perseo.restapi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import co.perseo.dtos.ServiceDTO;
import co.perseo.entities.Application;
import co.perseo.entities.Service;
import co.perseo.entities.ServiceGroup;
import co.perseo.inmemory.ServicesMap;
import co.perseo.publisher.ServicePublisher;
import co.perseo.response.SRResponse;
import co.perseo.service.PerseoService;
import co.perseo.util.AppPaths;

@RestController
@RequestMapping(AppPaths.SERVICE_REGISTRY_API_RESOURCE)
@Api(value = "Perseo controller")
public class PerseoController {

	private static final Logger logger = LogManager.getLogger(PerseoController.class);
	
	@Autowired
	private PerseoService srs;
	
	@Autowired
	private ServicePublisher publisher;
	
	@PostMapping(path = AppPaths.PUBLISH_SERVICE_RESOURCE_URL)
	public ResponseEntity<?> publishServiceInRegistry(@RequestBody ServiceDTO serviceDto){
		try {
			/**  **/
			if(!ServicesMap.instance().serviceExist(serviceDto))
			{
				ServicesMap.instance().addService(serviceDto);
				srs.registerService(serviceDto, null, null);
				publisher.publishService(serviceDto);
				
				logger.info("Services in Perseo: ");
				ServicesMap.instance().getServices().stream().forEach(service 
														      -> logger.info(service.getService_name()));
				
				return ResponseEntity.status(HttpStatus.OK).body("Service " + serviceDto.getService_name() 
				                             + " published in Registry.");
			}
			logger.info("servicio existente.");
			return ResponseEntity.status(HttpStatus.OK).body("Service " + serviceDto.getService_name() 
			                             + " already in Registry.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					             .body("Excepción publicando el nuevo servicio en la cola.\n"
					            	   + e.getLocalizedMessage());
		}
	}
	
	@GetMapping(path = AppPaths.SR_ALL_SERVICES_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllServices()
	{
		logger.info("Client requesting services list");
		return ResponseEntity.status(HttpStatus.OK).body(srs.getAllServices());
	}
	
	/**
	  * This method proxies every request to the appropriate backend service.
	  * @param request
	  * @param response
	  * @return
	*/	 
	@GetMapping(path = AppPaths.SR_SERVICES_LIST_BY_APP_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listServicesByApp(@PathVariable(value = "appId", required = true) String appId)
	{		
		logger.info("\n\nClient requesting services list for application ID: " + appId);
		
		List<ServiceDTO> services = srs.listServicesByApplicationId(appId);
		
		SRResponse resp = new SRResponse();
		
		if(services.isEmpty())
		{
			resp.setMessage("No services registered to application ID: " + appId);
			logger.info(resp.getMessage());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resp);
		}
		else
		{
			resp.setServices(services);
			resp.setMessage(services.size() + " services registered to applicatio ID " + appId);
			logger.info(resp.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(resp);
		}		
	}
	
	@GetMapping(path = AppPaths.SR_SERVICES_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getServiceById(@PathVariable(value = "serviceId", required = true) String serviceId)
	{		
		logger.info("\n\nClient requesting service by ID: " + serviceId);
		
		ServiceDTO service = srs.getService(serviceId);
		SRResponse resp = new SRResponse();
		
		if(service == null)
		{
			resp.setMessage("No service by ID: " + serviceId);
			logger.info(resp.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}
		else
		{
			List<ServiceDTO> services = new ArrayList<>();
			services.add(service);
			resp.setServices(services);
			resp.setMessage("Service " + service.getService_name() + " retrieved.");
			logger.info(resp.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(resp);
		}		
	}
	
	@PostMapping(path = AppPaths.SR_SERVICE_REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerService(@RequestBody Service service)
	{		
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	@DeleteMapping(path = AppPaths.SR_SERVICE_DEREGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deregisterService(@PathVariable(value = "serviceId", required = true) Integer serviceId)
	{		
		logger.info("\n\nClient requesting service deregistration for service ID: " + serviceId);
		
		SRResponse response = srs.deregisterService(serviceId);
		logger.info(response.getMessage());
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	@PostMapping(path = AppPaths.SR_SERVICE_GROUP_REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerServiceGroup(@RequestBody ServiceGroup serviceGroup, HttpServletRequest request)
	{		
		logger.info("\n\nClient requesting service group registration for service grou: " + serviceGroup.getService_group_name());
		
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	@PostMapping(path = AppPaths.SR_APPLICATION_REGISTRATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerApplication(@RequestBody Application application, HttpServletRequest request)
	{		
		logger.info("\n\nClient requesting application registration for application: " + application.getApplication_name());
		
		SRResponse response = null;
		try {
			response = srs.registerApplication(application);
			logger.info(response.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (SQLException e) {
			logger.error("SQL exception with message: " + e.getMessage());
			e.getStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}