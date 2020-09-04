package co.ias.perseo.entities;

import org.modelmapper.ModelMapper;

import co.ias.perseo.dtos.ApplicationDTO;
import co.ias.perseo.dtos.SRDto;
import co.ias.perseo.dtos.ServiceDTO;
import co.ias.perseo.dtos.ServiceGroupDTO;

public interface SREntity {
	default SRDto toDTO(SREntity entity){
		if(entity == null)
    		return null;
    	if(entity instanceof Service){
    		Service entity_1 = (Service)entity;
    		return new ModelMapper().map(entity_1, ServiceDTO.class);
    	}    		
    	else if(entity instanceof Application){
    		Application entity_1 = (Application)entity;
    		return new ModelMapper().map(entity_1, ApplicationDTO.class);
    	}
    	else{
    		ServiceGroup entity_1 = (ServiceGroup)entity;
    		return new ModelMapper().map(entity_1, ServiceGroupDTO.class);
        }
	}
}