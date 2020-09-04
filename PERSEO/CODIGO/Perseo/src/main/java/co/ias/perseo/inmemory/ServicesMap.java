package co.ias.perseo.inmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import co.ias.perseo.dtos.ServiceDTO;

public final class ServicesMap {
	/** Singleton **/
	private static ServicesMap instance;
	/** Mapa estático de servicios **/
	private static HashMap<String, ServiceDTO> serviceMap;
	
	private ServicesMap() {		
		serviceMap = new HashMap<>();
	}
	public static ServicesMap instance() {
		if(instance == null)
			instance = new ServicesMap();
		return instance;
	}
	
	/**
	 * Agrega el servicio al mapa, si no existe, y también a la base de datos.
	 * @param service
	 */
	public void addService(ServiceDTO service) {
		if(!serviceMap.containsKey(service.getService_name()))
			serviceMap.put(service.getService_name(), service);
	}
	
	public boolean serviceExist(ServiceDTO service) {
		return serviceMap.containsKey(service.getService_name());
	}
	
	/**
	 * 
	 * @param serviceList
	 */
	public void addServicesAtInitialization(List<ServiceDTO> serviceList) {
		serviceMap.putAll(serviceList.stream().filter(s -> s.isService_active())
				  .collect(Collectors.toMap(ServiceDTO::getService_name, Function.identity())));
	}
	public Collection<ServiceDTO> getServices() {
		return serviceMap.values();
	}
}