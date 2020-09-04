package co.ias.perseo.publisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.ias.perseo.dtos.ServiceDTO;

@Component
public class ServicePublisher {
	
	private static final Logger logger = LogManager.getLogger(ServicePublisher.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${jsa.rabbit.exchange}")
	private String exchange;
	
	@Value("${jsa.rabbit.routingkey}")
	private String routingkey;
	
	public void publishService(ServiceDTO serviceDto){
		amqpTemplate.convertAndSend(exchange, routingkey, serviceDto);
		logger.info("Service just published on queue:\n" + serviceDto);
	}
}