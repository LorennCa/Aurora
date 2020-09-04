package co.ias.usuarios.services;

import javax.servlet.http.HttpServletRequest;

import co.ias.common.response.ResourceResponse;
import co.ias.common.response.Response;
import co.ias.common.wrappers.AuthWrapper;
import co.ias.common.wrappers.UsuarioEntidadWrapper;
import co.ias.usuarios.config.appconfig.ServiceRegistrator;

public interface CerberoService extends ServiceRegistrator{
	
	public Response login(AuthWrapper authwrapper, HttpServletRequest request);

	public Response logout(HttpServletRequest request);
	
	public Response getUserId(String accessToken);

	public ResourceResponse validateUserEntity(UsuarioEntidadWrapper wrapper, Integer caso);
}