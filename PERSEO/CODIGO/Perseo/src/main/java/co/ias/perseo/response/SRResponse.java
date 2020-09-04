package co.ias.perseo.response;

import java.util.List;

import co.ias.perseo.dtos.ApplicationDTO;
import co.ias.perseo.dtos.SRDto;
import co.ias.perseo.dtos.ServiceDTO;
import co.ias.perseo.dtos.ServiceGroupDTO;

public class SRResponse {
	private String code;
	private String codeMessage;
	private String message;
	private SRDto dto;
	private List<ApplicationDTO> applications;
	private List<ServiceDTO> services;
	private List<ServiceGroupDTO> serviceGroups;
	
	public SRResponse(String id, String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public SRResponse() {
	}
	@Override
	public String toString() {
		return "SRResponse [code=" + code + ", message=" + message + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<ApplicationDTO> getApplications() {
		return applications;
	}
	public void setApplications(List<ApplicationDTO> applications) {
		this.applications = applications;
	}
	public List<ServiceDTO> getServices() {
		return services;
	}
	public void setServices(List<ServiceDTO> services) {
		this.services = services;
	}
	public List<ServiceGroupDTO> getServiceGroups() {
		return serviceGroups;
	}
	public void setServiceGroups(List<ServiceGroupDTO> serviceGroups) {
		this.serviceGroups = serviceGroups;
	}
	public SRDto getDto() {
		return dto;
	}
	public void setDto(SRDto dto) {
		this.dto = dto;
	}
	public String getCodeMessage() {
		return codeMessage;
	}
	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}
}