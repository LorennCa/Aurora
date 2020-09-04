package co.ias.usuarios.services;

import co.ias.common.entities.DynamicLink;
import co.ias.common.response.Response;
import co.ias.usuarios.config.appconfig.ServiceRegistrator;


public interface PasswordResetService extends ServiceRegistrator {
	
	/**
	 * 
	 * @param request
	 * @param usuario
	 * @param usuarioService 
	 * @return
	 */
	public Response resetUserPassword(String userLogin, String email);

	/**
	 * Devuelve el ID del usuario en el código de la respuesta si existe un usuario asociado al hashId de
	 * reinicialización de contraseña; si no, devuelve -1 si el hashId no existe.
	 * @param hashId
	 * @return
	 */
	public Response resetPassword(String hashId);
	
	/**
	 * Retorna la URL completa del sitio web del.
	 * @return
	 */
	public String getSEE_AplicationSiteURL();

	/**
	 * 
	 * @param login
	 * @return 
	 */
	public DynamicLink findPasswordReset(String userLogin);

	/**
	 * 
	 * @param userId
	 */
	public void deletePasswordReset(String userLogin);

	public void useAndExpirePassword(DynamicLink din);
}