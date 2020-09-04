package co.ias.usuarios.services;

import java.util.List;

import co.ias.common.entities.Entidad;
import co.ias.common.entities.Perfil;
import co.ias.common.entities.TipoDocumento;
import co.ias.common.entities.Usuario;
import co.ias.common.response.Response;
import co.ias.common.response.UsuarioLight;

public interface UserService {

	/**
	 * Consulta un usuario por credenciales de acceso
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public Usuario findByLogindAndPassword(String usuario, String password);

	/**
	 * Consulta usuario por userName/login
	 * 
	 * @param login
	 * @return
	 */
	public Usuario findByLogin(String login);

	/**
	 * Consulta un usuario por login y por estado. Este es usado para validar la
	 * creación de un nuevo usuario con un login (existente) pero el estado debe
	 * ser igual a INACTIVO
	 * 
	 * @param login
	 * @param estado
	 * @return
	 */
	public Usuario findByLoginAndEstadoNot(String login, String estado);

	/**
	 * Retorna todos los usuarios del sistema.
	 * 
	 * @return
	 */
	public List<Usuario> findAll();

	/**
	 * Buscar usuario por Identificador
	 * 
	 * @param id
	 * @return
	 */
	public Usuario findOne(Integer id);

	/**
	 * Este método únicamente guarda en la base de datos local, sin tener que ir
	 * a LDAP Gluu Server
	 * 
	 * @param usuarioFound
	 */
	public void save(Usuario usuario);

	/**
	 * Retorna únicamente los usuarios que pertenecen a una entidad que entra
	 * como parámetro
	 * 
	 * @param idEntidad
	 * @return
	 */
	public List<Usuario> findByEntidad(Integer idEntidad);

	/**
	 * True si es entidad Administradora
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean esUsuarioEntidadSuperAdmin(Usuario usuario);



	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean esUsuarioPerfilSuperAdministrador(Usuario usuario);

	/**
	 * Retorna todos los usuarios que no tienen un estado (Por ejemplo, lo que
	 * no están inactivos)
	 * 
	 * @param estado
	 * @return
	 */
	public List<Usuario> findAllByEstadoNot(String estado);

	/**
	 * Retorna todos los usuarios dado un perfil y con un estado en específico.
	 * 
	 * @param estado
	 * @return
	 */
	public List<Usuario> findByPerfilAndEstadoNot(Perfil perfil, String estado);

	/**
	 * Retorna true si la contraseña es nueva y no ha sido utilizada
	 * 
	 * @param usuario
	 * @param password
	 * @return
	 * @throws EEException
	 */
	public boolean passwordUnused(Usuario usuario, String password) throws Exception;

	/**
	 * Retorna false si el estado que entra como parámetro es diferente a activo
	 * 
	 * @param status
	 * @return
	 */
	public boolean homologateStatus(String status);

	/**
	 * Retorna un registro si existe un Usuario por número de documento, tipo
	 * Documento y entidad por parámetro
	 * 
	 * @param identificacion
	 * @param tipoDocumento
	 * @param entidad
	 * @return
	 */
	public List<Usuario> findByIdentificacionAndTipoDocumentoAndEntidadAndRol(
			String identificacion, TipoDocumento tipoDocumento, Entidad entidad,
			String rol);

	/**
	 * Retorna el listado de usuarios dado:
	 * 
	 * @param identificacion
	 * @param tipoDocumento
	 * @param entida
	 * @return
	 */
	public List<Usuario> findByIdentificacionAndTipoDocumentoAndEntidad(
			String identificacion, TipoDocumento tipoDocumento, Entidad entidad);

	/**
	 * Retorna la lista de Usuarios buscados por Id
	 * 
	 * @param ids
	 * @return
	 */
	public List<Usuario> findByIdIn(List<Integer> ids);

	/**
	 * Retorna una sugerencia de login en la funcionalidad de creación de
	 * usuarios en el sistema
	 * 
	 * @param loginExist
	 * @return
	 * @author Diego_Babativa
	 */
	public String suggestedLogin(String loginExist);

	/**
	 * Devuelve un usuario basado en su correo electrónico.
	 * @param correoe
	 * @return
	 */
	public Usuario findByEmail(String correoe);

	public void createNewAuthorizatorUser(Usuario usuario);

	public void deleteAuthorizatorUser(Usuario usuario);

	public Response resetPassword(String hashId);

	public List<UsuarioLight> parseUsuarioToUsuarioLight(List<Usuario> usuario);

	boolean esUsuarioEntidadEmisora(Usuario usuario);

	boolean esUsuarioEntidadAfiliadaMEC(Usuario usuario);

	boolean esUsuarioEntidadSCB(Usuario usuario);
}
