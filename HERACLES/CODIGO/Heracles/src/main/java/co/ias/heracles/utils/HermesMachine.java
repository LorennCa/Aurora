/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.heracles.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class HermesMachine {
	    
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
    
    public static String getMethodName(final int depth)
    {
      final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
      return ste[ste.length - 1 - depth].getMethodName();
    }
}