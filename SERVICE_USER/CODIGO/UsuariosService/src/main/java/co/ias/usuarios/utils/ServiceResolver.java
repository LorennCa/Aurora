package co.ias.usuarios.utils;


import static co.ias.usuarios.utils.AppConstants.*;

import org.springframework.stereotype.Component;

import co.ias.serviceregistry.inmemory.ServicesMap;
import co.ias.usuarios.dtos.ServiceDTO;


@Component
public class ServiceResolver {

	// @Autowired
	// private GWConfigurations confs;

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public String resolveServiceURL(String uri) {
		StringBuilder sb = new StringBuilder();
		String serviceUrl = null;
		ServiceDTO service = null;

		if (uri.indexOf(IAS_INITIAL_CONFIGURATION_CONTEXT) >= 0) {
			service = ServicesMap.instance().getService("HeraclesService");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context()).append(IAS_INITIAL_CONFIGURATION_CONTEXT);
		} else if ((uri.indexOf(GENERAL_ACCESS_CONTEXT) >= 0)) {
			service = ServicesMap.instance().getService("Usuarios");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context()).append(GENERAL_ACCESS_CONTEXT);

			if ((uri.indexOf(UPDATE_USER_PASSWORD_CONTEXT) >= 0) && (uri.indexOf(PASSWORD_HISTORY_CONTEXT) < 0)
					&& (uri.indexOf(PASSWORD_DICTIONARY_UPDATE_CONTEXT) < 0)) {
				sb.append(UPDATE_USER_PASSWORD_CONTEXT).append(SLASH);
				sb.append(uri.substring(uri.lastIndexOf("/") + 1, uri.length()));
			}

			else if (uri.indexOf(TOKEN_CONTEXT) >= 0)
				sb.append(TOKEN_CONTEXT);

			else if (uri.indexOf(PASSWORD_HISTORY_CONTEXT) >= 0)
				sb.append(PASSWORD_HISTORY_CONTEXT_PATH);

			else if (uri.indexOf(PASSWORD_DICTIONARY_UPDATE_CONTEXT) >= 0)
				sb.append(PASSWORD_DICTIONARY_UPDATE_CONTEXT);

			else if (uri.indexOf(VALIDATE_PROFILE_FOR_DELETE_CONTEXT) >= 0) {
				sb.append(VALIDATE_PROFILE_FOR_DELETE_CONTEXT);
				sb.append(uri.substring(uri.lastIndexOf("/") + 1, uri.length()));
			} else if (uri.indexOf(VALIDATE_USER_ENTITY_IDENTIFICATION_CONTEXT) >= 0)
				sb.append(VALIDATE_USER_ENTITY_IDENTIFICATION_CONTEXT);

			else if (uri.indexOf(IAS_GENERAL_ACCESS_VALIDATE_DATE_CONTEXT) >= 0)
				sb.append(IAS_GENERAL_ACCESS_VALIDATE_DATE_CONTEXT);
			else if (uri.indexOf(USUARIOS_VALIDAR_PERFILES_HIJOS) >= 0 )
				sb.append(USUARIOS_VALIDAR_PERFILES_HIJOS);
		} else if (uri.indexOf(PASSWORD_DICTIONARY_CONTEXT) >= 0) {
			service = ServicesMap.instance().getService("Usuarios");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context()).append(PASSWORD_DICTIONARY_CONTEXT);
		} else if (uri.indexOf(AUTHENTICATION_CONTEXT) >= 0) {
			service = ServicesMap.instance().getService("Usuarios");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context()).append(AUTHENTICATION_CONTEXT);

			if (uri.endsWith("login"))
				sb.append("/login");
			else if (uri.endsWith("logout"))
				sb.append("/logout");
		} else if (uri.indexOf(FILE_SERVICE_NAME_CONST) >= 0) {
			service = ServicesMap.instance().getService("FileService");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context());

			String fs = FILE_SERVICE_NAME_CONST;
			String fileServiceResourcePath = "/".concat(fs)
					.concat(uri.substring(uri.indexOf(fs) + fs.length(), uri.length()));
			sb.append(fileServiceResourcePath);

			serviceUrl = sb.toString();
		} else if (uri.indexOf(RESET_USER_PASSWORD_CONTEXT) >= 0) {
			service = ServicesMap.instance().getService("Usuarios");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
					.append(service.getService_host_ip()).append(":").append(service.getService_port())
					.append(service.getService_root_context()).append(RESET_USER_PASSWORD_CONTEXT);
		} else if(uri.indexOf("productos")>=0 || uri.indexOf("carrito")>=0|| uri.indexOf("precompra")>=0){
			service = ServicesMap.instance().getService("ecommerce");
			sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL).append(service.getService_host_ip()).append(":").append(service.getService_port()).append(uri.replaceAll("usuarios/", ""));
		}
			else {// Usuarios, Perfiles, dirigidos al
				// servicio ParamOperationService y a Cerbero, respectivamente

			if ((uri.indexOf("v1/usuarios") >= 0) || (uri.indexOf("v1/perfiles") >= 0)) {
				service = ServicesMap.instance().getService("Usuarios");
				sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
						.append(service.getService_host_ip()).append(":").append(service.getService_port())
						.append(uri.replaceAll("usuarios/", ""));
			} else {
				service = ServicesMap.instance().getService("ParamOperationService");
				sb.append(service.getService_protocol() != null ? service.getService_protocol() : DEFAULT_PROTOCOL)
						.append(service.getService_host_ip()).append(":").append(service.getService_port())
						.append(uri.replaceAll("IAS/", ""));

				service = null;
			}
		}
		serviceUrl = sb.toString();
		sb = null;
		service = null;
		return serviceUrl;
	}
}