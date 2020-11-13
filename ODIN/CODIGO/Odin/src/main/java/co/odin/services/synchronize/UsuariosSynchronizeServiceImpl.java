package co.odin.services.synchronize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import co.common.entities.Menu;
import co.common.entities.Perfil;
import co.common.entities.Usuario;
import co.common.response.ResourceResponse;
import co.common.wrappers.UsuarioEntidadWrapper;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.enums.EstadosUsuarioEnum;
import co.odin.restapi.UserController;
import co.odin.security.MasterSession;
import co.odin.services.CerberoService;
import co.odin.services.MenuService;
import co.odin.services.ProfileService;
import co.odin.services.UserService;
import co.odin.utils.HermesMachine;


@Component

public class UsuariosSynchronizeServiceImpl implements UsuariosSynchronizeService {

	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(UserController.class);

	private MessageSource mrc = MessageSourceConfig.messageSource();

	@Autowired
	private Environment springEnv;

	@Autowired
	private UsuariosTransactionalService cerberoTransService;

	@Autowired
	private CerberoService cerberoService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private MenuService menuService;

	/*
	 * 
	 * Metodos para la sincronizacion de usuarios Estos metodos llaman la clase
	 * transaccional del proyecto
	 */
	@Override
	public synchronized Usuario saveUser(Usuario usuario) throws ConstraintViolationException {
		return cerberoTransService.saveUser(usuario);
	}

	@Override
	public synchronized Usuario saveUserNew(Usuario usuario) throws Exception {

		UsuarioEntidadWrapper wrapper = new UsuarioEntidadWrapper();

		wrapper.setId(null);
		wrapper.setTipoDocumento(usuario.getTipoDocumento());
		wrapper.setNumeroDocumento(usuario.getNumeroIdentificacion());
		wrapper.setUsuarioRolEntidad(usuario.getUsuarioRolEntidad());

		/*
		 * Se envia 1 cuando es consulta al guardar 
		 * 0 cuando es consulta antes de guardar desde front*/
		ResourceResponse resourceResponse = cerberoService.validateUserEntity(wrapper, 1);
		//Usuario usuarioNew = new Usuario();
		/*
		 * 200, usuario no se puede crear, 204, usuario no existe, se puede
		 * crear
		 */
		if (resourceResponse.getCode().equals(HttpStatus.SC_NO_CONTENT)) {
			userService.createNewAuthorizatorUser(usuario);

			try {
				logger.info(mrc.getMessage("odin.recurso.POST", null, null), usuario.getLogin());
				//usuarioNew = saveUser(usuario);
				System.out.println("usuario-->"+usuario.getNumeroIdentificacion());
				System.out.println("usuario-->"+usuario.getPerfil());
				System.out.println("usuario-->"+usuario.getLogin());
				saveUser(usuario);
			}catch(DataIntegrityViolationException dive){
				logger.error("Error creando usuarios...",dive);
				throw new Exception (dive.getMessage());
		
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error guardar" + e);
				// Si falla el guardado del usuario en ee_usuario se borra el
				// usuario de la tabla authsee_app_user
				logger.info(mrc.getMessage("odin.recurso.DELETE", null, null), usuario.getLogin());
				String serviceUrlDel = HermesMachine.getServiceURL("tokenservice", "deleteuser", springEnv,
						usuario.getLogin());
				String tokenResponseDel = HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrlDel,
						HttpMethod.DELETE, String.class, null).getBody().toString();
				logger.info("Token service response: " + tokenResponseDel);
				throw new Exception(mrc.getMessage("odin.recurso.POST.ERROR", null, null));

			}

		} else {
			throw new Exception(mrc.getMessage("odin.usuario.business.validation.rolEntidad.ERROR", null, null));
		}
		return usuario;

	}

	@Override
	public synchronized Usuario saveUserMod(Usuario usuario) throws Exception {

		Usuario usuarioMod = new Usuario();
		/**
		 * Si el nuevo estado es "INACTIVO", se elimina el usuario de
		 * Autenticador Service para liberar el login (username) y se actualiza
		 * el usuario en base de datos interna (MySQL)
		 */
		
		if (usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.INACTIVO.name())
				|| usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.BLOQUEADO.name())) {
			try {
				userService.deleteAuthorizatorUser(usuario);
				usuarioMod =  saveUser(usuario);
			}catch(ConstraintViolationException cve){
				logger.error("Error creando usuarios...",cve);
				throw new Exception (cve.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error al guardar usuario bloqueado o Inactivo" + e);
				/* Se restable el usuario q fue borrado */
				userService.createNewAuthorizatorUser(usuario);
				throw new Exception(mrc.getMessage("odin.recurso.POST.ERROR", null, null));
			}
		}
		/*
		 * REVISIÓN REESTABLECIMIENTO DE CLAVES - se desbloquea entonces borra
		 * cualquiera reinicialización de contraseña
		 */
		else if (usuario.getEstado().equalsIgnoreCase(EstadosUsuarioEnum.ACTIVO.name())) {
			try {
				userService.createNewAuthorizatorUser(usuario);
				usuarioMod = cerberoTransService.saveUserMod(usuario);
			}catch(ConstraintViolationException cve){
				logger.error("Error creando usuarios...",cve);
				throw new Exception (cve.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error al guardar usuario activo" + e);
				/* Se borra usuario creado */
				userService.deleteAuthorizatorUser(usuario);
				throw new Exception(mrc.getMessage("odin.recurso.POST.ERROR", null, null));
			}

		}
		/**/
		else { // Actualiza la información de Usuario

			// Reiniciar el contador de intentos fallidos:
			if (MasterSession.instance().getSessionCounterMap().containsKey(usuario.getLogin())) {
				MasterSession.instance().getSessionCounterMap().put(usuario.getLogin(), 0);
			}

			try {
				usuarioMod = saveUser(usuario);
			}catch(ConstraintViolationException cve){
				logger.error("Error creando usuarios...",cve);
				throw new Exception (cve.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error al guardar usuario" + e);
				throw new Exception(mrc.getMessage("odin.recurso.POST.ERROR", null, null));
			}

		}
		return usuarioMod;

	}

	/*
	 * Metodos sincronizados para gestión de perfiles
	 */
	@Override
	public synchronized void savePerfil(Perfil perfil) {
		profileService.save(perfil);
	}

	@Override
	public synchronized void updateProfile(Perfil perfil) {
		// Evita Ids duplicados (Limita datos entrantes)
		Set<Integer> opcionesMenuTemp = new HashSet<Integer>();
		for (Menu menuTemp : perfil.getOpcionesMenu()) {
			if (menuTemp.getId() != null) {
				opcionesMenuTemp.add(menuTemp.getId());
			}
		}

		// Busca las opciones que se seleccionaron:
		List<Menu> menusNuevos = menuService.findByIdIn(opcionesMenuTemp);

		/*
		 * Remueve las opciones de menu únicamente si existen en base de datos
		 */
		if (perfil.getOpcionesMenu() != null && perfil.getOpcionesMenu().size() > 0) {

			// Se remueven las opciones existentes
			for (Menu menu : perfil.getOpcionesMenu()) {
				menu.getPerfiles().remove(perfil);
			}
			// Se guarda la actualización del perfil
			savePerfil(perfil);

			// Inicializa las opciones de Menu
			perfil.setOpcionesMenu(new ArrayList<Menu>());
		}

		// Asigna las copiones de Menu entrantes
		menuService.insertNewMenuToPerfil(perfil, menusNuevos);

		// Guarda perfil con nuevos permisos
		savePerfil(perfil);

	}

	@Override
	public synchronized void deletePerfil(Integer id) {
		profileService.delete(id);
	}

}
