package co.perseo.service;

import java.sql.SQLException;
import java.util.List;

import co.perseo.dtos.ServiceDTO;
import co.perseo.entities.Application;
import co.perseo.entities.ServiceGroup;
import co.perseo.response.SRResponse;

public interface PerseoService {
	
	public List<ServiceDTO> listServicesByApplicationId(String applicationId);
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	public ServiceDTO getService(String serviceId);
	
	public SRResponse registerService(ServiceDTO serviceDto, Application parentApp, ServiceGroup serviceGroup);
	
	/**
	 * 
	 * @param service
	 * @return
	 */
	public SRResponse deregisterService(Integer serviceId);

	/**
	 * Registers a service group.
	 * @param serviceGroup
	 * @return
	 */
	public SRResponse registerServiceGroup(ServiceGroup serviceGroup);

	/**
	 * Registers an application.
	 * @param application
	 * @return
	 * @throws SQLException 
	 */
	public SRResponse registerApplication(Application application) throws SQLException;
	
	public List<ServiceDTO> getAllServices();
}