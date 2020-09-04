package co.ias.usuarios.suscriber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import co.ias.serviceregistry.inmemory.ServicesMap;
import co.ias.usuarios.dtos.ServiceDTO;



@Component
public class ServiceSubscriber {
	private static final Logger logger = LogManager.getLogger(ServiceSubscriber.class);
	@RabbitListener(queues="${jsa.rabbit.queue}", containerFactory="jsaFactory")
    public void recievedMessage(ServiceDTO serviceDto) {
		logger.info("Service received for publication: " + serviceDto);
		ServicesMap.instance().addService(serviceDto);
		logger.info("Services in Gateway: ");
		ServicesMap.instance().getServices().stream().forEach(servicio -> logger.info(servicio.getService_name()));
   }
}