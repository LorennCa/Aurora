package co.ias.usuarios.services.impl;


import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.ias.common.custom.PerfilAbstract;
import co.ias.common.entities.Entidad;
import co.ias.common.entities.Perfil;
import co.ias.common.entities.TipoDocumento;
import co.ias.common.entities.Usuario;
import co.ias.common.response.Response;
import co.ias.common.response.UsuarioLight;
import co.ias.usuarios.config.appconfig.AppConfiguration;
import co.ias.usuarios.config.appconfig.MessageSourceConfig;
import co.ias.usuarios.enums.EstadosUsuarioEnum;
import co.ias.usuarios.enums.TipoEntidadEnum;
import co.ias.usuarios.repositories.UserOwnerUserRepository;
import co.ias.usuarios.repositories.UserRepository;
import co.ias.usuarios.services.UserService;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.UsuariosPasswordEncoder;
import co.ias.usuarios.utils.HermesMachine;

@Component
public class UserServiceImpl implements UserService {

	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	/**
	 * Inyección de dependencias
	 */
	@Autowired
	private UserRepository usuarioRepository;

	@Autowired
	private UserOwnerUserRepository usuarioOwnRepository;
	
	@Autowired
	private Environment springEnv;
	
	private MessageSource mrc = MessageSourceConfig.messageSource();

	@Override
	@Transactional(readOnly = true)
	public Usuario findByLogindAndPassword(String usuario, String password) {
		return usuarioOwnRepository.findByLoginAndPassword(usuario, password);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {

		// Retorna todos los usuarios excepto el persil super Administrador
		//List<Usuario> usuarios = (List<Usuario>) usuarioOwnRepository.findByIdNot(AppConstants.General.USUARIO_SUPER_ADMINISTRADOR_ID);
		List<Usuario> usuarios = usuarioOwnRepository.findAll();
		for (Usuario usuario : usuarios) {

			// To null el idLDAP y el password por seguridad
			usuario.setIdLDAP(null);
			usuario.setPassword("");
		}

		return usuarios;
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findOne(Integer id) {
		return usuarioRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByLogin(String login) {
		return usuarioOwnRepository.findByLogin(login);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByLoginAndEstadoNot(String login, String estado) {
		return usuarioOwnRepository.findByLoginAndEstadoNot(login, estado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findByEntidad(Integer idEntidad) {

		List<Usuario> usuarios = usuarioOwnRepository.findEntidad(idEntidad,
				AppConstants.General.USUARIO_SUPER_ADMINISTRADOR_ID);

		for (Usuario usuario : usuarios) {
			usuario.setIdLDAP(null);
			usuario.setPassword("");
			
		}
		return usuarios;
	}

	@Override
	public boolean esUsuarioEntidadSuperAdmin(Usuario usuario) {
		if (usuario.getUsuarioRolEntidad().getTipoRol().equalsIgnoreCase(TipoEntidadEnum.SUPER_ADMIN.name())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean esUsuarioEntidadEmisora(Usuario usuario) {
		if (usuario.getUsuarioRolEntidad().getTipoRol().equalsIgnoreCase(TipoEntidadEnum.EMISOR.name())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean esUsuarioEntidadAfiliadaMEC(Usuario usuario) {
		if (usuario.getUsuarioRolEntidad().getTipoRol().equalsIgnoreCase(TipoEntidadEnum.AFILIADO.name())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean esUsuarioEntidadSCB(Usuario usuario) {
		if (usuario.getUsuarioRolEntidad().getTipoRol().equalsIgnoreCase(TipoEntidadEnum.SCB.name())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean esUsuarioPerfilSuperAdministrador(Usuario usuario) {
		if (usuario.getPerfil().getId() == AppConstants.General.PERFIL_SUPER_ADMINISTRADOR_ID) {
			return true;
		}
		return false;
	}

	@Override
	public List<Usuario> findAllByEstadoNot(String estado) {
		return usuarioOwnRepository.findByEstadoNot(estado);
	}

	@Override
	public boolean passwordUnused(Usuario usuario, String password) throws Exception {
		// Consutla la N cantidad de intentos para reutilizar una contraseña.
		Integer passwordHistorialQuantity = AppConfiguration.intance().getSeeConfig().getIntentosReutilizarPassword();

		for (int i = 0; i < passwordHistorialQuantity.intValue(); i++) {

			if (usuario.getHistorialPassword().size() > i && UsuariosPasswordEncoder.validPassword(
					usuario.getHistorialPassword().get(i).getPassword(), password, AppConstants.General.SALT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean homologateStatus(String status) {
		if (status.trim().equalsIgnoreCase(EstadosUsuarioEnum.ACTIVO.name())) {
			return true;
		}
		return false;
	}

	@Override
	public List<Usuario> findByPerfilAndEstadoNot(Perfil perfil, String estado) {
		return usuarioOwnRepository.findByPerfilAndEstadoNot(perfil, estado);
	}

	@Override
	public synchronized List<Usuario> findByIdentificacionAndTipoDocumentoAndEntidadAndRol(String identificacion,
			TipoDocumento tipoDocumento, Entidad entidad, String tipoRol) {
		return usuarioOwnRepository.findUsuarioxTipoDocumentoxIdentificacionxEntidadxTipoRol(identificacion,
				tipoDocumento.getId(), entidad.getId(), tipoRol);
	}

	@Override
	public List<Usuario> findByIdIn(List<Integer> ids) {
		return usuarioOwnRepository.findByIdIn(ids);
	}
	
	public String suggestedLogin(String loginExist) {

		/**
		 * Se consulta el último usuario registrado con el login que se intenta
		 * usar
		 */
		Usuario usuario = usuarioOwnRepository.findByLogin(loginExist);

		StringBuilder logginSuggest = new StringBuilder();

		Usuario userFound = null;

		if (usuario != null) {

			// Obtiene el consecutivo del último login en base de datos
			String indexClear = usuario.getLogin().substring(loginExist.length(), usuario.getLogin().length());

			try {

				/**
				 * Para el caso que exista un único registro "login", se asigna
				 * el primer autoincremental (login1)
				 */
				if (indexClear.length() < 1 && indexClear.equalsIgnoreCase("")) {
					logginSuggest.append(loginExist).append(AppConstants.General.AUTO_INCREMENT_LOGIN);
				} else {

					int increment = 1;

					/**
					 * Pregunta tantas veces hasta que no retorne un usuario
					 * existente
					 */
					do {

						int index = Integer.parseInt(indexClear);
						logginSuggest.append(loginExist).append(index + increment);

						/*
						 * Se consulta el nuevo login sugerido para validar que
						 * no va a tener ningún problema con la inserción del
						 * nuevo usuario
						 */
						userFound = usuarioOwnRepository.findByLogin(logginSuggest.toString());

						increment++;

					} while (userFound != null);
				}

			} catch (NumberFormatException e) {
				logger.error("Hubo un error al obtener el índice de último login existente: {} ", e.getMessage());
				throw new RuntimeException(e);
			}
		}

		return logginSuggest.toString();
	}

	@Override
	public List<Usuario> findByIdentificacionAndTipoDocumentoAndEntidad(String identificacion,
			TipoDocumento tipoDocumento, Entidad entidad) {

		return usuarioOwnRepository.findUsuarioxTipoDocumentoxIdentificacionxEntidad(identificacion,
				tipoDocumento.getId(), entidad.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByEmail(String email) {
		return usuarioOwnRepository.findByEmail(email);
	}
	
	@Override
	public void createNewAuthorizatorUser(Usuario usuario) {
		logger.info(mrc.getMessage("ias.recurso.POST", null,null), usuario.getLogin());
		String serviceUrl = HermesMachine.getServiceURL("tokenservice", "newuser", springEnv,
														usuario.getLogin());
		String tokenResponse = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrl,
								HttpMethod.POST, String.class, null).getBody().toString();
		logger.info("Token service create UMA user response: " + tokenResponse);
	}
	
	@Override
	public void deleteAuthorizatorUser(Usuario usuario){
		logger.info(mrc.getMessage("ias.recurso.DELETE", null, null), usuario.getLogin());
		String serviceUrlDel = HermesMachine.getServiceURL("tokenservice", "deleteuser", springEnv,
				usuario.getLogin());
		String tokenResponseDel = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrlDel,
				HttpMethod.DELETE, String.class, null).getBody().toString();
		logger.info("Token service response: " + tokenResponseDel);
	}
	
	@Override
	public Response resetPassword(String hashId) {
		return null;
	}
	
	@Override
	public List<UsuarioLight> parseUsuarioToUsuarioLight(List<Usuario> usuarios){
		ArrayList<UsuarioLight> usuarioList = new ArrayList<UsuarioLight>();	
		for(Usuario usuario : usuarios){
			
			UsuarioLight usuarioLight = new UsuarioLight();
			PerfilAbstract perfil = new PerfilAbstract();
			PerfilAbstract perfilSuperior = new PerfilAbstract();

			usuarioLight.setId(usuario.getId());
			usuarioLight.setTipoDocumento(usuario.getTipoDocumento());
			usuarioLight.setNumeroIdentificacion(usuario.getNumeroIdentificacion());
			usuarioLight.setDv(usuario.getDv());
			usuarioLight.setNombres(usuario.getNombres());
			usuarioLight.setApellidos(usuario.getApellidos());
			usuarioLight.setEmail(usuario.getEmail());
			usuarioLight.setEstado(usuario.getEstado());
			usuarioLight.setLogin(usuario.getLogin());
			//Definir Perfil
			perfil.setId(usuario.getPerfil().getId());
			perfil.setNombre(usuario.getPerfil().getNombre());
			if(usuario.getPerfil().getPerfilSuperior() != null){
				perfilSuperior.setId(usuario.getPerfil().getPerfilSuperior().getId());
				perfilSuperior.setNombre(usuario.getPerfil().getPerfilSuperior().getNombre());
				perfil.setPerfilSuperior(perfilSuperior);
			}
			usuarioLight.setPerfil(perfil);
			usuarioLight.setListIpPermitidas(usuario.getListIpPermitidas());
			usuarioLight.setUsuarioRolEntidad(usuario.getUsuarioRolEntidad());
			
			usuarioList.add(usuarioLight);
		}

		return usuarioList;
		
	}
	
	
}