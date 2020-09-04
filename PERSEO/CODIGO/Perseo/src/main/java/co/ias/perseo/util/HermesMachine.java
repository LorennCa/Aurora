/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.perseo.util;

import org.modelmapper.ModelMapper;

import co.ias.perseo.dtos.ApplicationDTO;
import co.ias.perseo.dtos.SRDto;
import co.ias.perseo.dtos.ServiceDTO;
import co.ias.perseo.dtos.ServiceGroupDTO;
import co.ias.perseo.entities.Application;
import co.ias.perseo.entities.SREntity;
import co.ias.perseo.entities.Service;
import co.ias.perseo.entities.ServiceGroup;

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
    
    public static SRDto convertToDTO(SREntity entity)
    {    
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
