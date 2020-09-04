package co.com.datacof.fileservice;

import java.net.URI;
import java.util.Calendar;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import co.ias.aurora.perseo.ServiceDTO;
import co.ias.aurora.utils.AppConstants;

/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FileServiceImpl.class})*/
@SuppressWarnings("unused")
public class FileControllerTest {
	public static void main(String[] args) {
		ServiceDTO thisServiceDTO = new ServiceDTO();
	    thisServiceDTO.setService_active(true);
	    thisServiceDTO.setRegistration_date(Calendar.getInstance().getTime());
	    thisServiceDTO.setService_host_ip("13131");
	    thisServiceDTO.setService_name("FileService");
	    //thisServiceDTO.setService_root_context(AppConstants.SERVICE_ROOT_CONTEXT);
	    thisServiceDTO.setService_port("88881");
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    RestTemplate restTemplate = new RestTemplate();
	    HttpEntity<ServiceDTO> httpEntity = new HttpEntity<>(thisServiceDTO, headers);
	    String srServiceUrl = "http://localhost:10000/serviceRegistry/v1/publishService";
		ResponseEntity<?> response = restTemplate.exchange(srServiceUrl , HttpMethod.POST, httpEntity,
				     											String.class);
	    
		Object body = response.getBody();
	    System.out.println("Service Registry response: " + response.getBody());
	  
	}
	
}