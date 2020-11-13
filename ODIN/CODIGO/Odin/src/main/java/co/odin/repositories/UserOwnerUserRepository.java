package co.odin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.common.entities.Perfil;
import co.common.entities.Usuario;

public interface UserOwnerUserRepository extends JpaRepository<Usuario, Integer> {

	public Usuario findByLoginAndPassword(String login, String password);

	public Usuario findByLogin(String login);

	public Usuario findByLoginAndEstadoNot(String login, String estado);

	@Query(value = "select usuario.* "
			+ "from usuario usuario "
			+ "INNER JOIN usuario_rol_entidad entidadRol ON (usuario.cod_usuario_rol_entidad = entidadRol.id) "
			+ "where " + "entidadRol.cod_entidad = :idEntidad AND "
			+ "cod_perfil in :perfilesHijos AND estado <> 'INACTIVO'", nativeQuery = true)
	public List<Usuario> findEntidadAndPerfilesHijos(
			@Param("idEntidad") Integer idEntidad,
			@Param("perfilesHijos") List<Perfil> perfilesHijos);

	@Query(value = "select usuario.* "
			+ "from usuario usuario "
			+ "INNER JOIN usuario_rol_entidad entidadRol ON (usuario.cod_usuario_rol_entidad = entidadRol.id) "
			+ "where " + "entidadRol.cod_entidad = :idEntidad "
			+ "AND usuario.id NOT IN (:idUsuarioSuperAdministrador) ", nativeQuery = true)
	public List<Usuario> findEntidad(@Param("idEntidad") Integer idEntidad,
			@Param("idUsuarioSuperAdministrador") Integer idUsuarioAdministrador);

	public List<Usuario> findByEstadoNot(String estado);

	public List<Usuario> findByPerfilAndEstadoNot(Perfil perfil, String estado);

	@Query(value = "select u.* from usuario u, usuario_rol_entidad rol "
			+ "where u.cod_usuario_rol_entidad = rol.id and "
			+ "u.numero_identificacion = :identificacion and "
			+ "u.cod_tipo_documento= :tipoDocumento and "
			+ " rol.cod_entidad= :entidad and "
			+ " u.estado <> 'INACTIVO' and"
			+ " rol.tipo_rol = :tipoRol ", nativeQuery = true)
	public List<Usuario> findUsuarioxTipoDocumentoxIdentificacionxEntidadxTipoRol(
			@Param("identificacion") String identificacion,
			@Param("tipoDocumento") Integer tipoDocumento,
			@Param("entidad") Integer entidad, @Param("tipoRol") String tipoRol);

	@Query(value = "select u.* from usuario u, usuario_rol_entidad rol "
			+ "where u.cod_usuario_rol_entidad = rol.id and "
			+ "u.numero_identificacion = :identificacion and "
			+ "u.cod_tipo_documento= :tipoDocumento and "
			+ " rol.cod_entidad= :entidad "
			+ "and u.estado <> 'INACTIVO'", nativeQuery = true)
	public List<Usuario> findUsuarioxTipoDocumentoxIdentificacionxEntidad(
			@Param("identificacion") String identificacion,
			@Param("tipoDocumento") Integer tipoDocumento,
			@Param("entidad") Integer entidad);

	public List<Usuario> findByIdIn(List<Integer> ids);

	public List<Usuario> findByIdNot(Integer id);

	@Query(value = "SELECT usu " + "FROM Usuario usu " + "WHERE usu.id = "
			+ "( SELECT MAX(u.id) "
			+ "FROM Usuario u WHERE u.login like (:login%) )")
	public Usuario findByLoginStartingWith(@Param("login") String login);

	public Usuario findByEmail(String email);
}