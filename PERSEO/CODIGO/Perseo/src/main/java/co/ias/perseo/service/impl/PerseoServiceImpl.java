package co.ias.perseo.service.impl;

import static co.ias.perseo.util.HermesMachine.convertToDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import co.ias.perseo.dtos.ApplicationDTO;
import co.ias.perseo.dtos.ServiceDTO;
import co.ias.perseo.dtos.ServiceGroupDTO;
import co.ias.perseo.entities.Application;
import co.ias.perseo.entities.Service;
import co.ias.perseo.entities.ServiceGroup;
import co.ias.perseo.repository.ApplicationRepository;
import co.ias.perseo.repository.ServiceGroupRepository;
import co.ias.perseo.repository.ServiceRepository;
import co.ias.perseo.response.SRResponse;
import co.ias.perseo.service.PerseoService;
import co.ias.perseo.util.HermesMachine;

@Component
public class PerseoServiceImpl implements PerseoService {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ServiceGroupRepository serviceGroupRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Override
	public List<ServiceDTO> listServicesByApplicationId(String applicationId) {
		return serviceRepository.listServicesByApplicationId(applicationId).stream()
			   .map(service -> (ServiceDTO)convertToDTO(service)).collect(Collectors.toList());
	}

	@Override
	public ServiceDTO getService(String serviceId) {
		Service serviceEnt= serviceRepository.findOne(Integer.valueOf(serviceId));
		return (ServiceDTO) serviceEnt.toDTO(serviceEnt);
	}

	@Override
	public SRResponse registerService(ServiceDTO service, Application application, ServiceGroup serviceGroup) {
		SRResponse resp = new SRResponse();
			Service newService = serviceRepository.save(service.toEntity());
			resp.setCode("1");
			resp.setMessage("Service ".concat(newService.getService_name()).concat(" registered with ID: ").concat(newService.getService_id()+""));
		return resp;
	}

	@Override
	public SRResponse deregisterService(Integer serviceId) {
		SRResponse resp = new SRResponse();
		Service sr = serviceRepository.findOne(Integer.valueOf(serviceId));
		if(sr == null)
		{
			resp.setMessage("Service with ID ".concat(serviceId+"").concat(" does not exist"));
			resp.setCode("0");
		}
		else{
			serviceRepository.delete(sr);
			resp.setCode("1");
			resp.setMessage("Service ".concat(sr.getService_name()).concat(" deregistered."));
		}
		return resp;
	}

	@Override
	@Transactional
	public SRResponse registerServiceGroup(ServiceGroup serviceGroup) {
		SRResponse resp = new SRResponse();
		try{
			ServiceGroupDTO group = (ServiceGroupDTO) convertToDTO(serviceGroupRepository.saveAndFlush(serviceGroup));
			resp.setCode("1");
			resp.setCodeMessage("Operation successful");
			resp.setDto(group);
			resp.setMessage("Service group " + serviceGroup.getService_group_name() + " registered with ID: " + group.getService_group_id());
		}
		catch(DataAccessException dex)
		{
			resp.setCode("3");
			resp.setCodeMessage("Operation exceptioned");
			resp.setMessage("Exception writing data to db:\n" + dex.getLocalizedMessage());
		}
		return resp;
	}

	@Override
	public SRResponse registerApplication(Application application) throws SQLException {
		SRResponse resp = new SRResponse();
		try{
			Application app = applicationRepository.save(application);
			resp.setCode("1");
			resp.setCodeMessage("Operation successful");
			resp.setDto((ApplicationDTO)convertToDTO(app));
			resp.setMessage("Application " + application.getApplication_name() + " registered with ID: " + app.getApplication_id());
			resp.setDto(HermesMachine.convertToDTO(app));
		}
		catch(DataAccessException dex)
		{
			resp.setCode("3");
			resp.setCodeMessage("Operation exceptioned");
			resp.setMessage("Exception writing data to db:\n" + dex.getLocalizedMessage());
		}
		return resp;
	}

	@Override
	public List<ServiceDTO> getAllServices() {
		return serviceRepository.findAll().stream().map(s -> (ServiceDTO)s.toDTO(s)).collect(Collectors.toList());
	}
}