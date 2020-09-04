package co.ias.usuarios.restapi;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;

import static co.ias.usuarios.utils.HermesMachine.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import co.ias.common.response.Response;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.HermesMachine;
import co.ias.usuarios.utils.ServiceResolver;


@RestController
@RequestMapping(AppConstants.GATEWAY_ROOT_CONTEXT)
@Api(value = "Cerbero GW")
public class UsuariosGWController {

	private static final Logger logger = LogManager.getLogger(UsuariosGWController.class);
	
	//@Autowired
	//private ServicesConfig s;
	
	@Autowired
	private ServiceResolver serviceResolver;
	
	private HttpClient httpClient;
	
	@PostConstruct
	public void init() {
	    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	    httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}

	
	
	/**
	  * This method proxies every request to the appropriate backend service.
	  * @param request
	  * @param response
	  * @return
	*/	 
	@RequestMapping(value = "/**", method = {GET, POST, DELETE, PUT, OPTIONS, PATCH})
	@ResponseBody
	public ResponseEntity<?> proxyRequest(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="file", required=false) MultipartFile file)
	{

		String originalIP = HermesMachine.getIPFromRequest(request);
		logger.info("GWUtil.getIPFromRequest(request): " + originalIP);
		
		logger.info("\n\nClient " + request.getRemoteAddr() + " using " + request.getMethod() + " method for resource: " + request.getRequestURI());

		String uri = request.getRequestURI();
		String serviceUrl = serviceResolver.resolveServiceURL(uri);
		
		ResponseEntity<?> responsee = null;
		
		try {			
			String queryString = request.getQueryString();
			
			logger.info("Origin resource = " + serviceUrl + (queryString != null ? ("?" + queryString) : ""));
			//HttpUriRequest proxyRequest = createHttpRequest(request, serviceUrl);
			
			HttpResponse proxiedResponse = httpClient.execute(createHttpRequest(request, serviceUrl, file));
			
			
			
			if (queryString  != null && !queryString.isEmpty() && queryString.indexOf("listaIn") >= 0){
				
				proxiedResponse.getEntity().writeTo(response.getOutputStream());		
			}
					
			else
			{
				
				int responseStatusCode = proxiedResponse.getStatusLine().getStatusCode();
				
				if(proxiedResponse.getEntity() == null)
				{
					logger.info("No content response " + responseStatusCode);
					return new ResponseEntity<>(HttpStatus.valueOf(responseStatusCode));
				}
				responsee = new ResponseEntity<>(read(proxiedResponse.getEntity().getContent()),
												 makeResponseHeaders(proxiedResponse),
												 HttpStatus.valueOf(responseStatusCode));
				logger.info("Proxied response = " + responsee.getBody() +"\n");
			}
		}
		catch(Exception e){
			String errorMessage = "GW: excepción redirigiendo la solicitud\n" + e;
			Response errorResponse = new Response();
			errorResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			errorResponse.setMessage(errorMessage);
			logger.error(errorMessage);
			responsee = ResponseEntity.status(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR)
									  .body(HermesMachine.convertToJson(errorResponse));
		}		
		uri = serviceUrl = null;
	    return responsee;
	}
}