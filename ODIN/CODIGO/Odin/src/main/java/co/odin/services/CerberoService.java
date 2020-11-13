package co.odin.services;

import javax.servlet.http.HttpServletRequest;

import co.common.response.ResourceResponse;
import co.common.response.Response;
import co.common.wrappers.AuthWrapper;
import co.common.wrappers.UsuarioEntidadWrapper;
import co.odin.config.appconfig.ServiceRegistrator;

public interface CerberoService extends ServiceRegistrator{
	
	public Response login(AuthWrapper authwrapper, HttpServletRequest request);

	public Response logout(HttpServletRequest request);
	
	public Response getUserId(String accessToken);

	public ResourceResponse validateUserEntity(UsuarioEntidadWrapper wrapper, Integer caso);
}