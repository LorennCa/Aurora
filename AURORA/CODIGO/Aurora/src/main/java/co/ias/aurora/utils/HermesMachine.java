/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.aurora.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HermesMachine {
        /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth)
    {
      final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

      return ste[ste.length - 1 - depth].getMethodName(); 
    }
    
    public static boolean isThereNull(String... vars)
    {
    	for(String var : vars)
    		if(var == null)
    			return true;
    	return false;
    }
    
    /**
     * Consume un servicio web RESTful
     * @param headerContentType
     * @param serviceUrl
     * @param method
     * @param responseType
     * @param httpEntitty
     * @return
     */
    public static <T> ResponseEntity<?> consumeRestService(MediaType headerContentType, String serviceUrl, 
			 HttpMethod method, Class<T> responseType, Object httpEntitty)
    {
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(headerContentType);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> httpEntity = new HttpEntity<>(httpEntitty, headers);
		return restTemplate.exchange(serviceUrl , method, httpEntity, responseType);		
	}
}