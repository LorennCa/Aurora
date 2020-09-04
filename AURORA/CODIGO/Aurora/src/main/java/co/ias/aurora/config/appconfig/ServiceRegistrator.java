package co.ias.aurora.config.appconfig;



import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import co.ias.aurora.perseo.ServiceDTO;
import co.ias.aurora.utils.AppConstants;
import co.ias.aurora.utils.AppPaths;
import co.ias.aurora.utils.HermesMachine;

public interface ServiceRegistrator {
	
	/**
	 * This code executes once per service instance initialization.
	 * @param springEnv Spring environment expressed in the application.yaml file
	 * @param logger 
	 * @return whether or not is successfully initialized
	 * @throws Exception
	 */
	default String registerItself(Environment springEnv, Logger logger) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append(springEnv.getProperty("perseo.protocol")).append(springEnv.getProperty("perseo.host"))
		  .append(springEnv.getProperty("perseo.port")).append(springEnv.getProperty("perseo.context"))
		  .append(springEnv.getProperty("perseo.registerResource"));
		String srServiceUrl = sb.toString();
		
		logger.info("Service register at: " + srServiceUrl);
		
	    String serviceIp = null;
	    InetAddress ip;
		  try {
			ip = InetAddress.getLocalHost();
			serviceIp = ip.getHostAddress();
			logger.info("Current service IP address: " + serviceIp);
		  }catch (UnknownHostException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		  }
	    
	    ServiceDTO thisServiceDTO = new ServiceDTO();
	    thisServiceDTO.setService_active(true);
	    thisServiceDTO.setRegistration_date(Calendar.getInstance().getTime());
	    thisServiceDTO.setService_host_ip(serviceIp);
	    thisServiceDTO.setService_name(AppConstants.THIS_SERVICE_NAME);
	    thisServiceDTO.setService_root_context(AppPaths.SERVICE_ROOT_CONTEXT);
	    thisServiceDTO.setService_port(springEnv.getProperty("server.port"));
	    
	    ResponseEntity<?> response = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, srServiceUrl,
	    															  HttpMethod.POST,
	    															  String.class, thisServiceDTO);
	    
	    String body = (String) response.getBody();
	    logger.info("Service Registry response: " + body);
		return body;
	}
}