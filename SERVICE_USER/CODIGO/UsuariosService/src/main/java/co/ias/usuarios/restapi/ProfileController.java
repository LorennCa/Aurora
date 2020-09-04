package co.ias.usuarios.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static co.ias.usuarios.utils.AppConstants.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.Menu;
import co.ias.common.entities.Perfil;
import co.ias.common.entities.Usuario;
import co.ias.common.response.OtrasOpciones;
import co.ias.common.response.PerfilResponse;
import co.ias.common.response.ResourceResponse;
import co.ias.common.response.Response;
import co.ias.usuarios.config.appconfig.AppConfiguration;
import co.ias.usuarios.config.appconfig.MessageSourceConfig;
import co.ias.usuarios.enums.TipoMenuEnum;
import co.ias.usuarios.exception.UsuariosException;
import co.ias.usuarios.security.MasterSession;
import co.ias.usuarios.security.Session;
import co.ias.usuarios.services.MenuService;
import co.ias.usuarios.services.ProfileService;
import co.ias.usuarios.services.UserService;
import co.ias.usuarios.services.synchronize.UsuariosSynchronizeService;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.AppPaths;
import co.ias.usuarios.utils.UsuariosUtil;
import co.ias.usuarios.utils.HermesMachine;


@RestController
@RequestMapping(AppPaths.Perfil.IAS_PERFILES)
@Api(value = "Perfil Controller")
public class ProfileController {

	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(ProfileController.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private MessageSource mrc = MessageSourceConfig.messageSource();

	@Autowired
	private ProfileService profileService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private UserService userService;

	@Autowired
	private UsuariosSynchronizeService cerberoSynhService;

	@ApiOperation(value = AppPaths.Perfil.IAS_PERFILES, nickname = "perfiles", notes = "Trae toda la información de perfiles y permisos por perfil de inicio de sesión")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@ApiResponses(value = {
			@ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = "Ok", response = Response.class),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_PARTIAL_CONTENT, message = "Mensaje de validación"),
			@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Falla servidor") })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> get(HttpServletRequest request,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "id", required = false) Integer id) throws Exception {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Inicializar las variables como NO-CONTENT
			otrasOpciones.setPerfil(null);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse
					.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));

			// envia nuevamente el accessToken y el uri
			resourceResponse.setAccessToken(request.getAttribute(ACCESS_TOKEN_CONST).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			// Si viene el parámetro nombre, se busca perfil por nombre:
			if (null != nombre) {

				Perfil perfil = profileService.findByNombre(nombre);
				logger.info(mrc.getMessage("ias.recurso.GET.name", null, null), "Perfil", nombre);

				if (null != perfil) {

					Perfil perfilExistente = null;

					// Busca perfil existente por Id
					if (null != id) {
						perfilExistente = profileService.findOne(id);
					}

					if (null == id || (perfilExistente != null
							&& !perfilExistente.getNombre().toLowerCase().equalsIgnoreCase(nombre))) {

						PerfilResponse perfilResponse = new PerfilResponse();
						perfilResponse.setId(perfil.getId());
						perfilResponse.setNombre(perfil.getNombre());
						perfilResponse.setPerfilSuperior(perfil.getPerfilSuperior());
						perfilResponse.setOpcionesMenu(null);
						otrasOpciones.setPerfil(perfilResponse);
						resourceResponse.setOtrasOpciones(otrasOpciones);
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);

						resourceResponse
								.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

						return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);

					} else {
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
					}

				} else {
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
				}

			}

			// Consulta la sesión en el Map por accessToken como llave
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

			// Debo consultar en base de datos por id de Usuario:
			// Se hace asi porque el usuarios está sujeto a actualizaciones
			Usuario usuario = userService.findOne(session.getUsuario().getId());

			// Trae todas las opciones de menú existentes en SEE
			List<Menu> menuListAll = menuService.findByTipoMenuNot(TipoMenuEnum.CONSULTA.name());

			/*
			 * Construye el MenuTemplate que usa el Front para crear o
			 * actualizar un usuario
			 */
			MenuTemplate menutemplateAll = HermesMachine.construirMenuToUpdateOrCreatePerfil(menuListAll, null);
			otrasOpciones.setMenuOpciones(menutemplateAll);

			/**
			 * Trae listado de todos los perfiles sin opciones de Menú
			 */
			List<Perfil> perfilesList = new ArrayList<Perfil>();
			perfilesList = profileService.findAllWithoutMenuOpciones();

			// Para super Administradoras: TODOS los perfiles
			if (userService.esUsuarioEntidadSuperAdmin(usuario)) {
				perfilesList = profileService.findAllWithoutMenuOpciones();
			} else {
				// Trae únicamente los perfiles hijos del perfil actual
				perfilesList = profileService.findByPerfilSuperior(usuario.getPerfil());

				// Se agrega el perfil que inició sesión
				perfilesList.add(usuario.getPerfil());
			}

			otrasOpciones.setListaPerfil(perfilesList);

			// Obtiene la información de la sesión de usuario
			if (session != null && session.getUsuario() != null && session.getUsuario().getPerfil() != null) {

				otrasOpciones.setPermisos(
						AppConfiguration.intance().getPermisosPerfilXPerfilMap().get(usuario.getPerfil().getId()));

				resourceResponse.setOtrasOpciones(otrasOpciones);
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);

				resourceResponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

				logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

			} else {
				logger.error(mrc.getMessage("ias.security.autorizacion.resourceAccess.session.error", null, null));
			}
		} catch (Exception e) {
			new Exception(e);
			logger.info(mrc.getMessage("ias.recurso.POST.ERROR", null, null), "perfil", e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	/**
	 * Retorna un perfil por id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {
			AppPaths.ID_SIMPLE_VARIABLE_PATH_CONTEXT }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> get(@PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id, HttpServletRequest request) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {
			// envia nuevamente el accessToken
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			if (id != null) {

				Perfil perfil = profileService.findOne(id);
				logger.info(mrc.getMessage("ias.recurso.GET.id", null, null), "Perfil", id);

				if (null != perfil) {

					PerfilResponse perfilResponse = new PerfilResponse();

					perfilResponse.setId(perfil.getId());
					perfilResponse.setNombre(perfil.getNombre());
					perfilResponse.setPerfilSuperior(perfil.getPerfilSuperior());
					perfilResponse.setOpcionesMenu(
							AppConfiguration.intance().getMenuAllxPerfilToUpdate().get(perfilResponse.getId()));

					// Quitar datos no relevantes de Perfil padre
					if (perfilResponse.getPerfilSuperior() != null) {
						perfilResponse.getPerfilSuperior().setOpcionesMenu(null);
					}

					otrasOpciones.setPerfil(perfilResponse);
					resourceResponse.setOtrasOpciones(otrasOpciones);
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);

					resourceResponse
							.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

					return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
				}
			} else {
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		} catch (Exception e) {
			new UsuariosException(e);
		}
		resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
		resourceResponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
		logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
	}

	/**
	 * Método que atiene la creación de un nuevo perfil
	 * 
	 * @param token
	 * @return Validation
	 */

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Perfil perfil, HttpServletRequest request) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Envía nuevamente el accessToken y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			if (perfil != null) {

				logger.info(mrc.getMessage("ias.recurso.POST", null, null), "Perfil", perfil.getNombre());

				// Guarda lista de opciones a una temp:
				List<Menu> opcionesMenuListTemp = perfil.getOpcionesMenu();
				perfil.setOpcionesMenu(new ArrayList<Menu>());

				// Evita Ids duplicados
				Set<Integer> opcionesMenuSetTemp = new HashSet<Integer>();
				for (Menu menuTemp : opcionesMenuListTemp) {
					if (menuTemp.getId() != null) {
						opcionesMenuSetTemp.add(menuTemp.getId());
					}
				}

				// Lógica NUEVA DBABATIVA 15-09-2017
				List<Menu> menuList = menuService.findByIdIn(opcionesMenuSetTemp);

				/*
				 * Ingresa tods las opciones de Menu haciendo respectiva lógica
				 * de estructura de arbol
				 */
				menuService.insertNewMenuToPerfil(perfil, menuList);

				Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

				// Audita la creación del nuevo Perfil
				perfil.setUserAudit(session.getUsuario().getLogin());
				perfil.setIpAudit(request.getAttribute(AppConstants.General.AUTORIZACION_IP).toString());

				// Almacena el perfil junto a sus opcioens de Menú
				// profileService.save(perfil);
				cerberoSynhService.savePerfil(perfil);

				/*
				 * Construye a partir de las opciones de Menu, un MenuTemplate
				 * para el perfil sólo con las opciones de Menú relacionadas
				 */
				MenuTemplate mtMenuGeneral = UsuariosUtil.construirMenuGeneral(perfil.getOpcionesMenu());

				// Lo inserta en el Map de opciones de Menú General
				AppConfiguration.intance().getMenuGeneralXPerfilMap().put(perfil.getId(), mtMenuGeneral);

				// Lo inserta en el Map de permisos CRUD:
				List<Menu> todasOpcionesMenu = menuService.findByTipoMenuNot(TipoMenuEnum.CONSULTA.name());
				if (todasOpcionesMenu != null && todasOpcionesMenu.size() != 0) {
					AppConfiguration.intance().getPermisosPerfilXPerfilMap().put(perfil.getId(),
							UsuariosUtil.construirPermisosPerfilParaPerfil(todasOpcionesMenu, perfil.getOpcionesMenu()));
				}
				/*
				 * construye a partir de opciones de Menú, un MenuTemplate para
				 * el el perfil con TODAS las opciones de Menú. Si no están
				 * relacionadas, asigna un valor estado = false
				 */
				MenuTemplate mtMenuPerfilToUpdate = UsuariosUtil.construirMenuToUpdateOrCreatePerfil(todasOpcionesMenu,
						perfil.getOpcionesMenu());

				// Lo inserta en el Map de menusPerfilesToUpdate
				AppConfiguration.intance().getMenuAllxPerfilToUpdate().put(perfil.getId(), mtMenuPerfilToUpdate);

				// Envia el perfil que se acaba de crear en perfilList
				List<Perfil> perfilList = new ArrayList<Perfil>();
				perfilList.add(perfil);
				otrasOpciones.setListaPerfil(perfilList);

				resourceResponse.setOtrasOpciones(otrasOpciones);

				resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
				resourceResponse.setMessage(
						mrc.getMessage("ias.security.autorizacion.resourceAccess.POST.success", null, null));
				logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.POST.success", null, null));

			} else {
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		} catch (Exception e) {

			logger.error(mrc.getMessage("ias.recurso.POST.ERROR", null, null), "Perfil", perfil.getNombre(),
					e.getMessage());

			if (e.getCause().getCause().getMessage().contains(AppConstants.General.VALIDATE_NOMBRE_UNIQUE)) {

				resourceResponse.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);
				resourceResponse.setMessage(mrc.getMessage("ias.POST.perfil.business.validation.name", null, null));

				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resourceResponse);

			}
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	/**
	 * Método que atiene la actualización de un perfil
	 * 
	 * @param token
	 * @return Validation
	 */
	@RequestMapping(value = {
			AppPaths.ID_SIMPLE_VARIABLE_PATH_CONTEXT }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody Perfil perfil, HttpServletRequest request,
			@PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Envía nuevamente el accessToken y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			if (null != id) {

				// busca el perfil en base de datos
				Perfil perfilFound = profileService.findOne(id);

				if (null != perfilFound) {

					logger.info(mrc.getMessage("ias.recurso.PUT", null, null), "Perfil", perfilFound.getId(),
							perfilFound.getNombre());

					perfilFound.setNombre(perfil.getNombre());
					perfilFound.setDescripcion(perfil.getDescripcion());

					// Establece el perfilsuperior
					Perfil perfilsuperior = profileService.findOne(perfil.getPerfilSuperior().getId());
					perfilFound.setPerfilSuperior(perfilsuperior);

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
					 * Remueve las opciones de menu únicamente si existen en
					 * base de datos
					 */
					if (perfilFound.getOpcionesMenu() != null && perfilFound.getOpcionesMenu().size() > 0) {

						// Se remueven las opciones existentes
						for (Menu menu : perfilFound.getOpcionesMenu()) {
							menu.getPerfiles().remove(perfilFound);
						}
						// Se guarda la actualización del perfil
						// profileService.save(perfilFound);
						cerberoSynhService.savePerfil(perfilFound);

						// Inicializa las opciones de Menu
						perfilFound.setOpcionesMenu(new ArrayList<Menu>());
					}

					// Asigna las copiones de Menu entrantes
					menuService.insertNewMenuToPerfil(perfilFound, menusNuevos);

					Session session = MasterSession.instance().getSessionTokenMap()
							.get(resourceResponse.getAccessToken());

					// Audita los cambios realziados al perfil
					perfilFound.setUserAudit(session.getUsuario().getLogin());
					perfilFound.setIpAudit(request.getAttribute(AppConstants.General.AUTORIZACION_IP).toString());

					logger.info(mrc.getMessage("ias.recurso.PUT", null, null), "perfil", perfilFound.getId(),
							perfilFound.getNombre());

					logger.info("Va a almacenar estas opciones de Menu: {}", perfilFound.getOpcionesMenu());

					// Guarda el perfil actualizado
					// profileService.save(perfilFound);
					cerberoSynhService.savePerfil(perfilFound);

					logger.info("Guardo las opciones de menu");

					/*
					 * Construye a partir de las opciones de Menu, un
					 * MenuTemplate para el perfil sólo con las opciones de Menú
					 * relacionadas
					 */
					MenuTemplate mtMenuGeneralActualizado = UsuariosUtil
							.construirMenuGeneral(perfilFound.getOpcionesMenu());

					// Se actualiza el map con el menuTemplate actualizado:
					AppConfiguration.intance().getMenuGeneralXPerfilMap().put(perfilFound.getId(),
							mtMenuGeneralActualizado);

					// Lo inserta en el Map de permisos CRUD:
					List<Menu> todasOpcionesMenu = menuService.findByTipoMenuNot(TipoMenuEnum.CONSULTA.name());

					/*
					 * Actualizo los permisos para perfil de acuerdo a lo
					 * indicado por el usuario
					 */
					if (todasOpcionesMenu != null && todasOpcionesMenu.size() != 0) {
						AppConfiguration.intance().getPermisosPerfilXPerfilMap().put(perfilFound.getId(), UsuariosUtil
								.construirPermisosPerfilParaPerfil(todasOpcionesMenu, perfilFound.getOpcionesMenu()));
					}

					/*
					 * construye a partir de opciones de Menú, un MenuTemplate
					 * para el el perfil con TODAS las opciones de Menú. Si no
					 * están relacionadas, asigna un valor estado = false
					 */
					MenuTemplate mtMenuPerfilToUpdateActualizado = UsuariosUtil
							.construirMenuToUpdateOrCreatePerfil(todasOpcionesMenu, perfilFound.getOpcionesMenu());

					// Se actualiza el Map de menusPerfilesToUpdate
					AppConfiguration.intance().getMenuAllxPerfilToUpdate().put(perfilFound.getId(),
							mtMenuPerfilToUpdateActualizado);

					// Envia el perfil que se acaba de crear en perfilList
					List<Perfil> perfilList = new ArrayList<Perfil>();
					perfilList.add(perfilFound);
					otrasOpciones.setListaPerfil(perfilList);

					resourceResponse.setOtrasOpciones(otrasOpciones);
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
					resourceResponse.setMessage(
							mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.success", null, null));
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.success", null, null));
					return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
				}
			} else {
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}
		} catch (Exception e) {
			logger.error(mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.error", null, null),
					e.getMessage());
			new UsuariosException(e);
		}
		resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
		resourceResponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
		logger.error(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
	}

	/**
	 * Método que atiende la eliminación de un usuario.
	 * 
	 * @param id
	 *            de Usuario
	 * @return
	 */
	@RequestMapping(value = {
			AppPaths.ID_SIMPLE_VARIABLE_PATH_CONTEXT }, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(
			// SEE-2018-01-24
			HttpServletRequest request, @PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id) {

		ResourceResponse resourceResponse = new ResourceResponse();

		try {

			// SEE-2018-01-24
			// Envía nuevamente el accessToken y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);
			// fin
			if (id != null) {

				Perfil perfilFound = profileService.findOne(id);

				if (null != perfilFound) {
					//Validacion para que no se pueda eliminar un perfil que tiene perfiles hijos sin usuarios
					List<Perfil> perfilSuperior = profileService.findByPerfilSuperior(perfilFound);
					if (perfilSuperior.size() > 0) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);
						resourceResponse.setMessage(mrc.getMessage("ias.PerfilSuperior.DELETE.ERROR", null, null));
						logger.info(mrc.getMessage("ias.PerfilSuperior.DELETE.ERROR", null, null));

						return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resourceResponse);

					} else {

						// Eliminar primero las opciones de Menú referenciados:
						for (Menu menu : perfilFound.getOpcionesMenu()) {
							Menu menuFound = menuService.findOne(menu.getId());
							if (null != menuFound)
								menuFound.getPerfiles().remove(perfilFound);
						}
						logger.info(mrc.getMessage("ias.recurso.DELETE", null, null), "Perfil", perfilFound.getId(),
								perfilFound.getNombre());

						// Elimina el perfil de base de datos
						// profileService.delete(id);
						cerberoSynhService.deletePerfil(id);

						/**
						 * En el map de Perfiles, se elimina este por
						 * identificador
						 */
						AppConfiguration.intance().getMenuGeneralXPerfilMap().remove(perfilFound.getId());

						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse.setMessage(
								mrc.getMessage("ias.security.autorizacion.resourceAccess.DELETE.success", null, null));
						logger.info(
								mrc.getMessage("ias.security.autorizacion.resourceAccess.DELETE.success", null, null));

						return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
					}

				}
			} else {
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}
		} catch (Exception e) {
			resourceResponse.setMessage(mrc.getMessage("ias.perfil.DELETE.ERROR", null, null));
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.error(mrc.getMessage("ias.perfil.DELETE.ERROR", null, null), id);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResourceResponse.getSingleInstance());
		}
		// Ajusta salida de un recurso no encontrado
		UsuariosUtil.resourceNotFound();
		logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null), id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResourceResponse.getSingleInstance());
	}

	/**
	 * Genera el reporte de los Perfiles existentes en el ias. Puede ser 1 o más
	 * Perfiles. Se generan en formato XLSX - Perfil por hoja del libro de
	 * trabajo
	 * 
	 * @throws IOException Get XLSX -report @return @throws
	 */
	@RequestMapping(value = "/XLSX", method = RequestMethod.GET)
	public @ResponseBody void getFileXLSX(HttpServletResponse response,
			@RequestParam(name = "listaIn") List<Integer> listaIn) throws IOException {

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=reportes_perfiles.xlsx");

			List<Perfil> perfilesToReport = new ArrayList<Perfil>();
			perfilesToReport = profileService.findByIdIn(listaIn);

			// se establecen las cabeceras
			String[] headers = new String[] { "Módulos y Funciones Asignados", "Perfil Superior" };

			// Se crea el libro de cálculo
			XSSFWorkbook workbook = new XSSFWorkbook();

			/**
			 * Por cada perfil, se crea una hoja de trabajo
			 */
			for (Perfil perfil : perfilesToReport) {

				PerfilResponse perfilResponse = new PerfilResponse();
				perfilResponse
						.setOpcionesMenu(AppConfiguration.intance().getMenuAllxPerfilToUpdate().get(perfil.getId()));

				XSSFSheet sheet = workbook.createSheet(perfil.getNombre());

				XSSFRow tittleRow = sheet.createRow(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 4));

				XSSFCell tittleCell = tittleRow.createCell(0);

				tittleCell.setCellValue(AppConstants.TITULO_REPORTE_PERFILES);

				XSSFRow tittleRow1 = sheet.createRow(2);

				XSSFCell tittleCell1 = tittleRow1.createCell(0);

				tittleCell1.setCellValue("En este reporte solo aparecen los permisos habilitados del perfil");

				XSSFRow rowHeaderPerfilSuperior = sheet.createRow(4);

				XSSFRow rowHeader = sheet.createRow(5);
				XSSFFont font = null;
				font = workbook.createFont();
				font.setBold(true);
				font.setColor(HSSFColor.WHITE.index);

				/**
				 * Establece el estilo de las opciones de Nivel I
				 */
				CellStyle headerStyleLevelI = workbook.createCellStyle();
				headerStyleLevelI.setFillBackgroundColor(HSSFColor.BLUE.index);
				headerStyleLevelI.setFillPattern(FillPatternType.BIG_SPOTS);
				headerStyleLevelI.setFont(font);
				headerStyleLevelI.setWrapText(true);

				// Asigna estilo al titulo
				tittleCell.setCellStyle(headerStyleLevelI);
				tittleCell1.setCellStyle(headerStyleLevelI);
				/**
				 * Establece la cabecera para el campo: Módulos y funciones
				 */
				XSSFCell cell = rowHeader.createCell(0);
				cell.setCellValue(headers[0]);
				cell.setCellStyle(headerStyleLevelI);

				/**
				 * Establece las cabeceras para el perfil superior
				 */
				XSSFCell cellPerfilsuperior = rowHeaderPerfilSuperior.createCell(0);
				cellPerfilsuperior.setCellValue(headers[1]);
				cellPerfilsuperior.setCellStyle(headerStyleLevelI);
				rowHeaderPerfilSuperior.createCell(2).setCellValue(perfil.getPerfilSuperior().getNombre());

				/**
				 * Establece el estilo para las opciones de Nivel II
				 */
				CellStyle headerStyleLevelII = workbook.createCellStyle();
				headerStyleLevelII.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);

				/**
				 * Establece el estilo para las opciones de Nivel III
				 */
				CellStyle headerStyleLevelIII = workbook.createCellStyle();
				headerStyleLevelIII.setFillBackgroundColor(HSSFColor.AQUA.index);

				/**
				 * Inicia a escribir el nombre de las opciones de Menú desde la
				 * fila 4
				 */
				int rowIndex = 6;

				if (perfilResponse.getOpcionesMenu() != null && perfilResponse.getOpcionesMenu().getChildren() != null
						&& perfilResponse.getOpcionesMenu().getChildren().size() > 0) {

					for (MenuTemplate menuTemplateLevelI : perfilResponse.getOpcionesMenu().getChildren()) {

						XSSFRow rowDataLevelI = sheet.createRow(rowIndex);

						if (menuTemplateLevelI.getIsSelected()) {

							XSSFCell cellLevelI = rowDataLevelI.createCell(1);
							cellLevelI.setCellValue(menuTemplateLevelI.getLabel());
							cellLevelI.setCellStyle(headerStyleLevelI);

							rowIndex++;
						}

						if (menuTemplateLevelI.getChildren() != null && menuTemplateLevelI.getChildren().size() > 0) {

							for (MenuTemplate menuTemplateLevelII : menuTemplateLevelI.getChildren()) {

								XSSFRow rowDataLevelII = sheet.createRow(rowIndex);

								if (menuTemplateLevelII.getIsSelected()) {

									XSSFCell cellLevelII = rowDataLevelII.createCell(2);
									cellLevelII.setCellValue(menuTemplateLevelII.getLabel());
									cellLevelII.setCellStyle(headerStyleLevelII);

									rowIndex++;
								}

								if (menuTemplateLevelII.getChildren() != null
										&& menuTemplateLevelI.getChildren().size() > 0) {

									for (MenuTemplate menuTemplateLevelIII : menuTemplateLevelII.getChildren()) {

										XSSFRow rowDataLevelIII = sheet.createRow(rowIndex);

										if (menuTemplateLevelIII.getIsSelected()) {

											XSSFCell cellLevelIII = rowDataLevelIII.createCell(2);
											cellLevelIII.setCellValue(" - " + menuTemplateLevelIII.getLabel());
											cellLevelIII.setCellStyle(headerStyleLevelIII);

											rowIndex++;
										}

									}
								}

							}
						}
						rowIndex++;
					}
				}
				// Ajusta el tamaño de las columnas
				for (int i = 0; i < 4; i++) {
					sheet.autoSizeColumn(i);
				}
			}

			workbook.write(response.getOutputStream());
			workbook.close();

			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.info", null, null),
					" Perfiles - XLSX");

		} catch (Exception e) {
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.error", null, null),
					" Perfiles - XLSX", e.getMessage());
		}
	}

	/**
	 * Genera el reporte de los Perfiles existentes en el ias. Puede ser 1 o más
	 * Perfiles. Se generan en formato zip
	 * 
	 * @throws IOException Get PDF -(package ZIP file) report @return @throws
	 */
	@RequestMapping(value = "/PDF-ZIP", method = RequestMethod.GET, produces = "application/zip")
	public @ResponseBody void getFilePDFZip(HttpServletResponse response,
			@RequestParam(name = "listaIn") List<Integer> listaIn) throws IOException {

		try {

			List<Perfil> perfilesToReport = new ArrayList<Perfil>();
			perfilesToReport = profileService.findByIdIn(listaIn);

			// Adiciona la compresión de la sallida de archivos
			response.addHeader("Content-Disposition", "attachment; filename=\"perfiles_pdf.zip\"");

			ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

			for (Perfil perfil : perfilesToReport) {

				PerfilResponse perfilResponse = new PerfilResponse();

				perfilResponse.setId(perfil.getId());
				perfilResponse.setNombre(perfil.getNombre());
				perfilResponse.setOpcionesMenu(
						AppConfiguration.intance().getMenuAllxPerfilToUpdate().get(perfilResponse.getId()));

				logger.info("Generando reporte para: {}", perfil.getNombre());

				JRDataSource jrDataSource = new JRBeanCollectionDataSource(
						perfilResponse.getOpcionesMenu().getChildren());

				InputStream jasperStream = resourceLoader.getResource("classpath:reports/ReportePerfil.jrxml")
						.getInputStream();
				JasperDesign design = JRXmlLoader.load(jasperStream);
				JasperReport report = JasperCompileManager.compileReport(design);

				// Map to hold Jasper Report Parameters
				Map<String, Object> parameterMap = new HashMap<String, Object>();

				// Se envía la informaciÓN
				parameterMap.put("id", perfilResponse.getId());
				parameterMap.put("nombre", perfilResponse.getNombre());
				parameterMap.put("datasource", jrDataSource);

				// Se envía el formato XLS para que SI imprima la fecha
				parameterMap.put("format", "PDF");

				JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameterMap, jrDataSource);

				byte bytes[] = JasperExportManager.exportReportToPdf(jasperPrint);

				zipOutputStream.putNextEntry(new ZipEntry("ReportePerfil_" + perfilResponse.getNombre() + ".pdf"));
				zipOutputStream.write(bytes);
				zipOutputStream.closeEntry();

				// Cierra el stream abierto
				jasperStream.close();
			}

			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.info", null, null),
					"Perfiles - PDF");

			zipOutputStream.close();

		} catch (JRException e) {
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.error", null, null),
					"Perfiles - PDF", e.getMessage());
		}
	}

}
