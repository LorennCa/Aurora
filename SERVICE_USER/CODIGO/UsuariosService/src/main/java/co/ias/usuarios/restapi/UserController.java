package co.ias.usuarios.restapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ias.common.entities.DiccionarioClaves;
import co.ias.common.entities.IpPermitida;
import co.ias.common.entities.Perfil;
import co.ias.common.entities.Usuario;
import co.ias.common.entities.UsuarioPasswordHistorial;
import co.ias.common.entities.UsuarioRolEntidad;
import co.ias.common.response.Entidades;
import co.ias.common.response.OtrasOpciones;
import co.ias.common.response.ResourceResponse;
import co.ias.common.response.Response;
import co.ias.common.response.UsuarioLight;
import co.ias.common.wrappers.ConfirmPasswordWrapper;
import co.ias.usuarios.config.appconfig.MessageSourceConfig;
import co.ias.usuarios.exception.UsuariosException;
import co.ias.usuarios.security.MasterSession;
import co.ias.usuarios.security.Session;
import co.ias.usuarios.services.EntidadService;
import co.ias.usuarios.services.EntidadesService;
import co.ias.usuarios.services.MenuTemplateService;
import co.ias.usuarios.services.PasswordDictionaryService;
import co.ias.usuarios.services.ProfileService;
import co.ias.usuarios.services.TipoDocumentoService;
import co.ias.usuarios.services.UserService;
import co.ias.usuarios.services.UsuarioRolEntidadService;
import co.ias.usuarios.services.synchronize.UsuariosSynchronizeService;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.AppPaths;
import co.ias.usuarios.utils.UsuariosPasswordEncoder;
import co.ias.usuarios.utils.UsuariosUtil;


@RestController
@RequestMapping(AppPaths.Usuarios.IAS_USUARIOS)
@Api(value = "Usuario Controller")
public class UserController {

	/**
	 * variable de log4j para logs de la aplicación
	 */
	private static final Logger logger = LogManager.getLogger(UserController.class);

	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private MessageSource mrc = MessageSourceConfig.messageSource();

	// ***********************************
	// Inyección de dependencias
	//
	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private EntidadService entidadService;

	@Autowired
	private EntidadesService entidadesService;

	@Autowired
	private MenuTemplateService menuTemplateService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private Environment springEnv;

	@Autowired
	private UsuarioRolEntidadService usuarioRolEntidadService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private PasswordDictionaryService pdservice;

	@Autowired
	private UsuariosSynchronizeService cerberoSynhService;

	/**
	 * Atiende la solicitud de consulta de usuarios
	 * 
	 * @param request
	 * @param nombre
	 * @return
	 * @throws EEException
	 */
	@ApiOperation(value = AppPaths.Usuarios.IAS_USUARIOS, nickname = "usuarios", notes = "Trae toda la información de usuarios")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "resourceAccessWrapper", value = "JSON ResourceAccessWrapper", required = true, dataType = MediaType.APPLICATION_JSON_VALUE) })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> get(HttpServletRequest request,
			@RequestParam(value = "nombre", required = false) String nombre) throws UsuariosException {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Inicializar las variables como NO-CONTENT
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse
					.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));

			// envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);

			// ****************************************************************
			// Consulta por Login y ESTADO inactivo
			if (null != nombre) {

				Usuario usuario = userService.findByLogin(nombre);

				if (null != usuario) {

					// Set the Response 
					otrasOpciones.setUsuario(usuario);
					resourceResponse.setOtrasOpciones(otrasOpciones);
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);

					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

					// Dado que el login ya existe en el sistema, se retorna una
					// sugerencia al usuario
					resourceResponse
							.setMessage(mrc.getMessage("ias.POST.usuario.business.validation.login.suggest", null, null)
									+ userService.suggestedLogin(nombre));

					return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
				} else {
					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
				}
			}
			// FIN consulta por Login
			// ****************************************************************

			// Consulta la sesión en el Map por accessToken como llave
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

			// /***************************************************************
			// Retorna la lista de usuarios, perfiles y entidades de acuerdo al
			// tipo de entidad a la que pertenece el usuario

			List<Usuario> usuariosList = new ArrayList<Usuario>();
			List<Perfil> perfilesList = new ArrayList<Perfil>();
			Entidades entidades = new Entidades();

			// Debo consultar en base de datos por id de Usuario:
			// Se hace asi porque el usuarios está sujeto a actualizaciones
			Usuario usuario = userService.findOne(session.getUsuario().getId());

			// Trae la información de perfil
			Perfil perfil = profileService.findOne(usuario.getPerfil().getId());

			if (userService.esUsuarioEntidadSuperAdmin(usuario)
					|| userService.esUsuarioPerfilSuperAdministrador(usuario)) {

				// Envía toda la info
				usuariosList = userService.findAll();
				
				perfilesList = profileService.findAll();
				
				entidades.setSuperAdministrador(entidadService.findBySuperAdminTrue());
				entidades.setEmisores(entidadService.findByEmisorTrue());

			} else {

				// Envía información parcial:
				// * Perfiles hijos de su perfil asignado
				// * Usuarios que son de su misma entidad

				perfilesList = profileService.findByPerfilSuperior(usuario.getPerfil());
				
				/**
				 * Se agrega el perfil que inició sesión
				 */
				perfilesList.add(usuario.getPerfil());

				usuariosList = userService.findByEntidad(usuario.getUsuarioRolEntidad().getEntidad().getId());
				entidades = entidadesService.setEntidadesXUsuarioRolEntidad(usuario);
			}

			// ****************************************************************
			// Retorna los permisos que tiene sobre la administración de
			// usuarios

			otrasOpciones
					.setPermisos(menuTemplateService.getPermisosByPerfil(perfil, AppConstants.General.CADENA_USUARIO));
			otrasOpciones.setTipoDocumento(tipoDocumentoService.findAll());

			// *****************************************************************
			// Retorna toda la información de diccionario de contraseñas
			// Retorna un único registro
			// String cadenaClaves =
			// getRestrictedWordList(resourceResponse.getAccessToken());
			// Retorna un único registro
			DiccionarioClaves diccionario = pdservice.findAll().get(0);

			// DiccionarioClaves diccionario =
			// diccionaroClavesService.findAll().get(0);

			if (null != diccionario)
				otrasOpciones.setDiccionario(diccionario.getCadenaClaves());
			// *****************************************************************

			// Asigna los objetos de respuesta de la solicitud
			//Response para UsuarioLigth
			List<UsuarioLight> usuarioResumen = userService.parseUsuarioToUsuarioLight(usuariosList);
			
			otrasOpciones.setListaUsuarios(usuarioResumen);
			
			for(Perfil perfilAbstract : perfilesList){
				
				perfilAbstract.setPerfilSuperior(null);
				perfilAbstract.setOpcionesMenu(null);	
			}
			
			otrasOpciones.setListaPerfil(perfilesList);
			otrasOpciones.setEntidades(entidades);

			resourceResponse.setOtrasOpciones(otrasOpciones);
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));

		} catch (Exception e) {
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("ias.recurso.GET.ERROR", null, null), "Usuario", "Nombre", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	/**
	 * Retorna la validación de password que se realiza desde la venta de
	 * ADMINISTRACIÓN DE USUARIOS
	 * 
	 * 
	 * @param id
	 * @return ResponseEntity validation
	 */
	@RequestMapping(value = {
			AppPaths.Usuarios.IAS_USUARIOS_VALIDATE_PASSWORD }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validatePassword(@RequestBody(required = true) ConfirmPasswordWrapper passwordWrapper,
			HttpServletRequest request) throws Exception {

		ResourceResponse resourceResponse = new ResourceResponse();

		try {

			// Inicializar las variables como NO-CONTENT
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_NO_CONTENT);
			resourceResponse
					.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));

			// Envia nuevamente el accessToken y uri
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);

			if (null != passwordWrapper.getId()) {

				if (null != passwordWrapper.getOldPass() && !passwordWrapper.getOldPass().isEmpty()
						&& passwordWrapper.getOldPass().length() > 0) {

					Usuario usuarioFound = userService.findOne(passwordWrapper.getId());

					if (UsuariosPasswordEncoder.isPasswordValid(usuarioFound.getPassword(), passwordWrapper.getOldPass(),
							AppConstants.General.SALT)) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse
								.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
					} else {
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
					}

				}

				if (null != passwordWrapper.getNewPass() && !passwordWrapper.getNewPass().isEmpty()
						&& passwordWrapper.getNewPass().length() > 0) {

					Usuario usuario = userService.findOne(passwordWrapper.getId());
					logger.info("Ultimas 3 Contraseñas: ");
					logger.info(usuario.getHistorialPassword());
					if (userService.passwordUnused(usuario, passwordWrapper.getNewPass())) {
						resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
						resourceResponse
								.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.OK", null, null));
					} else {
						logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.NOT_FOUND", null, null));
						return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceResponse);
					}
				}

			} else {
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		} catch (Exception e) {

			logger.info(mrc.getMessage("ias.recurso.GET.ERROR", null, null), "Usuario", passwordWrapper.getId(),
					e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);

	}

	/**
	 * Método que atiende la creación de un nuevo recurso de tipo Usuario
	 * 
	 * @param token
	 * @return Validation
	 */
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody Usuario usuario, HttpServletRequest request) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();

		try {

			// Envía nuevamente el accessToken y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Usuarios.PATH);

			logger.info(mrc.getMessage("ias.recurso.POST", null, null), "Usuario", usuario.getLogin());

			List<IpPermitida> ipsToInsert = new ArrayList<IpPermitida>();
			

			/**
			 * Recorre las IPs que ingresó el usuario:
			 */
			for (IpPermitida ipPermitida : usuario.getListIpPermitidas()) {

				if (null != ipPermitida && ipPermitida.getIp() != null && ipPermitida.getIp().length() > 0
						&& !ipPermitida.getIp().isEmpty()) {
					IpPermitida ipNew = new IpPermitida();
					ipNew.setIp(ipPermitida.getIp());
					ipNew.setUsuario(usuario);
					ipsToInsert.add(ipNew);
				}
			}

			// Establece las Ips al usuario para ser insertadas
			usuario.setListIpPermitidas(ipsToInsert);

			// Trae la información del usuario para guardar la auditoría
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());
			usuario.setUserAudit(session.getUsuario().getLogin());
			usuario.setIpAudit(request.getAttribute(AppConstants.General.AUTORIZACION_IP).toString());

			// Establece contraseña y LDAP generado por Gluu server
			usuario.setPassword(
					UsuariosPasswordEncoder.encodePassword(usuario.getPassword(), AppConstants.General.SALT));

			// ******************************************************************
			// Establece los objetos complejos asociados a la entidad Usuario

			// Establece el persil asociado
			usuario.setPerfil(profileService.findOne(usuario.getPerfil().getId()));
			System.out.println("perfil establecido-->"+profileService.findOne(usuario.getPerfil().getId()));
			// Establece el tipo Documento
			usuario.setTipoDocumento(tipoDocumentoService.findOne(usuario.getTipoDocumento().getId()));
			System.out.println("tipoDocumento-->"+tipoDocumentoService.findOne(usuario.getTipoDocumento().getId()));

			// Adicional el password en el histórico de contraseñas
			List<UsuarioPasswordHistorial> usuPassHisList = new ArrayList<UsuarioPasswordHistorial>();
			UsuarioPasswordHistorial usuPassHist = new UsuarioPasswordHistorial();
			usuPassHist.setFecha(new Date());
			usuPassHist.setPassword(usuario.getPassword());
			System.out.println("contrasena establecida"+usuario.getPassword().toString());
			usuPassHist.setUsuario(usuario);
			usuPassHisList.add(usuPassHist);
			

			// Establece el histórico de contraseñas
			usuario.setHistorialPassword(usuPassHisList);

			// Asigna el Rol que juega una Entidad para dicho usuario
			List<UsuarioRolEntidad> usuRolEntList = usuarioRolEntidadService.findByEntidadAndTipoRol(
					usuario.getUsuarioRolEntidad().getEntidad(), usuario.getUsuarioRolEntidad().getTipoRol());

			// Si encuentra un Rol y una Entidad existente, se la asigna
			if (null != usuRolEntList && usuRolEntList.size() != 0) {
				usuario.setUsuarioRolEntidad(usuRolEntList.get(0));
			}

			// Habilita el cambio de contraseña obligatorio
			usuario.setCambioClave(Boolean.TRUE);

			// Actualiza el último cambio de clave:
			usuario.setUltimoCambioClave(new Date());
			// Agrega un @inum ó id de LDAP quemado:
			usuario.setIdLDAP("@!InumQuemado-No-creado-por-Gluu-Server");
			// tokenService.createNewUser(usuario);
			// createNewUMAUser(usuario);
			// userService.save(usuario);

			/*
			 * En el guardado del usuario se hace nuevamente la validacion de
			 * usuario rol entidad se pueden obtener dos respuestas del metodo
			 * Usuario creado exitosamente, businessErrorCode = 100 tipo y
			 * numero de documento de usuario ya existe en el tipo de entidad o
			 * Emisor Exception httpCode = 200 y business ErrorCode = 101
			 */
			List<Usuario> usuariosList = new ArrayList<Usuario>();
			usuariosList.add(0, cerberoSynhService.saveUserNew(usuario));
			
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.POST.success", null, null));
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);
			resourceResponse
					.setMessage(mrc.getMessage("ias.security.autorizacion.resourceAccess.POST.success", null, null));

				
			/*
			 * Envia todos los usuarios de acuerdo al tipo de entidad al que
			 * este atado el usuarios
			 */
//			if (userService.esUsuarioEntidadSuperAdmin(usuario)
//					|| userService.esUsuarioPerfilSuperAdministrador(usuario)) {
//				// Envía toda la info
//				usuariosList = userService.findAll();
//
//			} else {
//				usuariosList = userService.findByEntidad(usuario.getUsuarioRolEntidad().getEntidad().getId());
//			}
			
			
			//Response para UsuarioLigth
			List<UsuarioLight> usuarioResumen = userService.parseUsuarioToUsuarioLight(usuariosList);
			

			for (Usuario user : usuariosList) {
				user.setPassword("");
				user.setHistorialPassword(new ArrayList<UsuarioPasswordHistorial>());
			}

			//otrasOpciones.setListaUsuarios(usuariosList);			
			otrasOpciones.setListaUsuarios(usuarioResumen);
			resourceResponse.setOtrasOpciones(otrasOpciones);

		} catch (Exception e) {

			logger.error(mrc.getMessage("ias.recurso.POST.ERROR", null, null), "Usuario", usuario.getLogin(),
					e.getMessage());

			if (e.getMessage().contains(AppConstants.General.VALIDATE_LOGIN_UNIQUE)
					|| e.getMessage().contains(AppConstants.General.VALIDATE_LOGIN_UNIQUE_MYSQL)) {

				resourceResponse.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);
				resourceResponse.setMessage(mrc.getMessage("ias.POST.usuario.business.validation.login", null, null));

				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resourceResponse);

			} else if (e.getMessage()
					.contains(mrc.getMessage("ias.usuario.business.validation.rolEntidad.ERROR", null, null))) {
				// otrasOpciones.setBusinessErrorCode(AppConstants.SEE_USER_ROL_ENTIDAD_FOUND);
				// resourceResponse.setOtrasOpciones(otrasOpciones);
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_PRECONDITION_FAILED);
				resourceResponse
						.setMessage(mrc.getMessage("ias.usuario.business.validation.rolEntidad.ERROR", null, null));

				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resourceResponse);

			}

			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);
			resourceResponse.setMessage(e.getMessage());
			logger.info(mrc.getMessage("ias.recurso.POST.ERROR", null, null), "Usuario", usuario.getNombres(),
					e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	// private void createNewUMAUser(Usuario usuario) {
	// String serviceUrl = HermesMachine.getServiceURL("tokenservice",
	// "newuser", springEnv,
	// usuario.getLogin());
	// String tokenResponse =
	// HermesMachine.consumeRestService(MediaType.APPLICATION_JSON, serviceUrl,
	// HttpMethod.POST, String.class, null).getBody().toString();
	// logger.info("Token service create UMA user response: " + tokenResponse);
	// }

	/**
	 * Método que atiene la actualización de un usuario
	 * 
	 * @param token
	 * @return Validation
	 */
	@RequestMapping(value = {
			AppPaths.ID_SIMPLE_VARIABLE_PATH_CONTEXT }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody(required = false) Usuario usuario, HttpServletRequest request,
			@PathVariable(AppPaths.PATH_VARIABLE_ID) Integer id) {

		ResourceResponse resourceResponse = new ResourceResponse();
		OtrasOpciones otrasOpciones = new OtrasOpciones();
		Usuario usuarioFound = null;
		try {

			// Envía nuevamente el accessToken y el recurso
			resourceResponse.setAccessToken(request.getAttribute(AppConstants.General.ACCESS_TOKEN_VALUE).toString());
			resourceResponse.setResource(AppPaths.Perfil.PATH);

			logger.info(mrc.getMessage("ias.recurso.PUT", null, null), "Usuario", usuario.getId(), usuario.getLogin());

			// Trae la información del usuario para guardar la auditoría
			Session session = MasterSession.instance().getSessionTokenMap().get(resourceResponse.getAccessToken());

			if (null != id) {

				usuarioFound = userService.findOne(id);

				if (null != usuarioFound) {

					// Si viene el usuario con un id distinto de null, actualiza
					// únicamente contraseña ó estado
					if (usuario.getId() != null) {

						if (usuario.getPassword() != null && usuario.getPassword().length() > 0
								&& !usuario.getPassword().isEmpty()) {

							// Actualiza el password codificado
							usuarioFound.setPassword(UsuariosPasswordEncoder.encodePassword(usuario.getPassword(),
									AppConstants.General.SALT));

							/*
							 * Si el usuario quien actualiza la contraseña no es
							 * el mismo propietario de la misma, se procede a
							 * activar el cambio de contraseña oblgatorio
							 */

							if (usuario.getId().intValue() != session.getUsuario().getId().intValue()) {
								// Establece el cambio de clave obligatorio
								usuarioFound.setCambioClave(Boolean.TRUE);
							} else {
								// No es obligatorio el cambio de clave
								usuarioFound.setCambioClave(Boolean.FALSE);
							}

							// Actualiza el último cambio de clave:
							usuarioFound.setUltimoCambioClave(new Date());

							// Agrega la contraseña actualizada al historial
							UsuarioPasswordHistorial newUsuPassHist = new UsuarioPasswordHistorial();
							newUsuPassHist.setFecha(new Date());
							newUsuPassHist.setPassword(usuarioFound.getPassword());
							newUsuPassHist.setUsuario(usuarioFound);
							usuarioFound.getHistorialPassword().add(newUsuPassHist);

						}

						if (usuario.getEstado() != null && usuario.getEstado().length() > 0
								&& !usuario.getEstado().isEmpty()) {
							usuarioFound.setEstado(usuario.getEstado());
						}

						// Actualiza toda la información del Usuario
					} else {

						// Copia la lista de ips permitidas almacenadas
						List<IpPermitida> clone = new ArrayList<IpPermitida>();
						clone.addAll(usuarioFound.getListIpPermitidas());

						// ******************************************************
						// Begin: Actualización de IPs permitidas

						// Elimina las IPs que no estén en las nuevas que entran
						for (IpPermitida ipPermitida : clone) {
							if (!UsuariosUtil.existsInList(usuario.getListIpPermitidas(), ipPermitida.getIp())) {
								usuarioFound.getListIpPermitidas().remove(ipPermitida);
							}
						}

						// Agrega las nuevas que no estén en el listado actual
						for (IpPermitida ipEntrante : usuario.getListIpPermitidas()) {
							if (ipEntrante.getIp() != null && ipEntrante.getIp().length() > 0) {
								if (!UsuariosUtil.existsInList(clone, ipEntrante.getIp())) {
									IpPermitida ipNew = new IpPermitida();
									ipNew.setIp(ipEntrante.getIp());
									ipNew.setUsuario(usuarioFound);
									usuarioFound.getListIpPermitidas().add(ipNew);
								}
							}
						}
						// END: Actualización de IPs permitidas
						// ******************************************************************

						// ******************************************************************
						// Establece los objetos complejos asociados a la
						// entidad Usuario Establece el perfil asociado
						usuarioFound.setPerfil(profileService.findOne(usuario.getPerfil().getId()));

						// Establece el tipo Documento
						usuarioFound.setTipoDocumento(tipoDocumentoService.findOne(usuario.getTipoDocumento().getId()));

						// Establece información adicional
						usuarioFound.setNumeroIdentificacion(usuario.getNumeroIdentificacion());
						usuarioFound.setNombres(usuario.getNombres());
						usuarioFound.setApellidos(usuario.getApellidos());
						usuarioFound.setEmail(usuario.getEmail());
						usuarioFound.setDv(usuario.getDv());
						usuarioFound.setEstado(usuario.getEstado());

						// Busca un Rol por entidad y tipo de rol
						List<UsuarioRolEntidad> usuRolEntList = usuarioRolEntidadService.findByEntidadAndTipoRol(
								usuario.getUsuarioRolEntidad().getEntidad(),
								usuario.getUsuarioRolEntidad().getTipoRol());

						/*
						 * Si encuentra un Rol y una Entidad existente, se la
						 * asigna
						 */
						if (null != usuRolEntList && usuRolEntList.size() != 0) {
							usuarioFound.setUsuarioRolEntidad(usuRolEntList.get(0));
						} else {
							usuarioFound.setUsuarioRolEntidad(usuario.getUsuarioRolEntidad());
						}

					}

					// Audita la actualización de Usuario
					usuarioFound.setUserAudit(session.getUsuario().getLogin());
					usuarioFound.setIpAudit(request.getAttribute(AppConstants.General.AUTORIZACION_IP).toString());

					// aca se llama al servicio Sincronizado para guardar
					// usuario
					List<Usuario> usuariosList = new ArrayList<Usuario>();
					usuariosList.add(0, cerberoSynhService.saveUserMod(usuarioFound));


					logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.success", null, null));
					resourceResponse.setCode(org.apache.http.HttpStatus.SC_OK);					

//					/*
//					 * Envia todos los usuarios de acuerdo al tipo de entidad al
//					 * que este atado el usuarios
//					 */
//					if (userService.esUsuarioEntidadSuperAdmin(usuarioFound)
//							|| userService.esUsuarioPerfilSuperAdministrador(usuarioFound)) {
//						// Envía toda la info
//						usuariosList = userService.findAll();
//
//					} else {
//						usuariosList = userService
//								.findByEntidad(usuarioFound.getUsuarioRolEntidad().getEntidad().getId());
//					}

					for (Usuario user : usuariosList) {
						user.setPassword("");
						user.setHistorialPassword(new ArrayList<UsuarioPasswordHistorial>());
					}

					//otrasOpciones.setListaUsuarios(usuariosList);
					//Response para UsuarioLigth
					List<UsuarioLight> usuarioResumen = userService.parseUsuarioToUsuarioLight(usuariosList);
					
					otrasOpciones.setListaUsuarios(usuarioResumen);
					resourceResponse.setOtrasOpciones(otrasOpciones);
					resourceResponse.setMessage(
							mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.success", null, null));
					if (MasterSession.instance().getSessionCounterMap().containsKey(usuarioFound.getLogin())) {
						MasterSession.instance().getSessionCounterMap().put(usuarioFound.getLogin(), 0);
					}
				}
			} else {
				logger.info(mrc.getMessage("ias.recurso.params.error", null, null));
				resourceResponse.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
				resourceResponse.setMessage(mrc.getMessage("ias.recurso.params.error", null, null));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceResponse);
			}

		} catch (Exception e) {
			logger.info(mrc.getMessage("ias.recurso.PUT.ERROR", null, null), "Usuario", usuarioFound.getId(),
					usuarioFound.getNombres(), e.getMessage());
			resourceResponse.setCode(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);

			resourceResponse.setMessage(
					mrc.getMessage("ias.security.autorizacion.resourceAccess.PUT.error", null, null) + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resourceResponse);
		}

		return ResponseEntity.status(HttpStatus.OK).body(resourceResponse);
	}

	/**
	 * Método que atiende la eliminación de un usuario.
	 * 
	 * @param id
	 *            de Usuario
	 * @return
	 */
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Genera el reporte de los Usuarios existentes en el ias. Puede ser 1 o más
	 * Perfiles. Se generan en formato PDF
	 * 
	 * @throws IOException
	 *             Get PDF file report @return @throws
	 */
	@RequestMapping(value = "/PDF", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody void getFilePDF(HttpServletResponse response,
			@RequestParam(name = "listaIn") List<Integer> listaIn) throws IOException {

		try {
			// Lista de usuarios
			List<Usuario> usuarios = userService.findByIdIn(listaIn);

			// Convertir List a JRBeanCollectionDatasource
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(usuarios);

			// Map to hold Jasper Report Parameters
			Map<String, Object> parameterMap = new HashMap<String, Object>();

			parameterMap.put("itemDataSource", itemsJRBean);

			// Se envía el formato XLS para que no imprima la fecha
			parameterMap.put("format", "PDF");

			// Using compiled version (.jasper) to Jasper Report to generate PDF
			// file
			InputStream jasperStream = resourceLoader.getResource("classpath:reports/UsuariosReporte.jrxml")
					.getInputStream();

			JasperDesign design = JRXmlLoader.load(jasperStream);

			JasperReport report = JasperCompileManager.compileReport(design);

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameterMap, new JREmptyDataSource());

			response.setHeader("Content-Disposition", "inline; filename=file.pdf");
			response.setContentType("application/pdf");

			final OutputStream outPutStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outPutStream);

			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.info", null, null),
					"Usuarios - PDF");

			// Se cierra el stream abierto
			jasperStream.close();

		} catch (JRException e) {
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.error", null, null),
					"Perfiles - PDF", e.getMessage());
		}

	}

	/**
	 * Genera el reporte de los Usuarios existentes en el ias. Puede ser 1 o más
	 * usuarios. Se generan en formato EXCEL a través de Apache POI
	 * 
	 * @throws IOException
	 *             Get EXCEL file report @return @throws
	 */
	@RequestMapping(value = "/XLSX", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody void getFileXLSX(HttpServletResponse response,
			@RequestParam(name = "listaIn") List<Integer> listaIn) throws IOException {

		try {

			// Lista de usuarios
			List<Usuario> usuarios = userService.findByIdIn(listaIn);

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=monFichier.xlsx");

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("usuarios_SEE");

			String[] headers = new String[] { "Tipo de Documento", "Número de Documento", "Nombres", "Apellidos",
					"Login", "Entidad asociada", "Correo electrónico", "Perfil", "Estado" };

			XSSFRow tittleRow = sheet.createRow(1);

			XSSFCell tittleCell = tittleRow.createCell(0);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			tittleCell.setCellValue(AppConstants.TITULO_REPORTE_USUARIOS);

			XSSFRow rowHeader = sheet.createRow(2);

			/**
			 * Personaliza la línea de cabecera del archivo
			 */
			CellStyle headerStyle = workbook.createCellStyle();

			XSSFFont font = null;
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(HSSFColor.WHITE.index);
			headerStyle.setFont(font);
			headerStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
			headerStyle.setFillPattern(FillPatternType.BIG_SPOTS);

			// Estilo para titulo Reporte
			tittleCell.setCellStyle(headerStyle);

			// Establece las cabeceras de la tabla de usuarios
			XSSFCell cell = null;
			/**
			 * Writing Headers
			 */
			int colNum = 0;
			for (String header : headers) {
				cell = rowHeader.createCell(colNum);
				cell.setCellValue(header);
				cell.setCellStyle(headerStyle);
				++colNum;
			}

			int index = 3;

			for (Usuario usuario : usuarios) {

				XSSFRow rowData = sheet.createRow(index);

				// Establece cada uno de los datos del usuario en la columna
				// correspondiente
				rowData.createCell(0).setCellValue(usuario.getTipoDocumento().getNombre());
				rowData.createCell(1).setCellValue(usuario.getNumeroIdentificacion());
				rowData.createCell(2).setCellValue(usuario.getNombres());
				rowData.createCell(3).setCellValue(usuario.getApellidos());
				rowData.createCell(4).setCellValue(usuario.getLogin());
				rowData.createCell(5).setCellValue(usuario.getUsuarioRolEntidad().getEntidad().getNombre());
				rowData.createCell(6).setCellValue(usuario.getEmail());
				rowData.createCell(7).setCellValue(usuario.getPerfil().getNombre());
				rowData.createCell(8).setCellValue(usuario.getEstado());

				index++;

			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
			workbook.close();

			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.info", null, null),
					"Usuarios - XLSX");

		} catch (Exception e) {
			logger.info(mrc.getMessage("ias.security.autorizacion.resourceAccess.reporte.error", null, null),
					"Usuarios - XLS", e.getMessage());
		}
	}

	/**
	 * 
	 * @param request
	 * @param correoe
	 * @return
	 */
	public ResponseEntity<?> resetUserPassword(HttpServletRequest request,
			@RequestParam(value = "login", required = true) String login) {
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param correoe
	 * @return
	 */
	// @Deprecated
	@ApiOperation(value = AppPaths.Usuarios.SEND_PASS_RESET_EMAIL, nickname = "sendReset", notes = "Se encarga del envío de correo con link de cambio de contraseña")
	@GetMapping(value = AppPaths.Usuarios.SEND_PASS_RESET_EMAIL)
	public ResponseEntity<?> sendResetEmail(HttpServletRequest request,
			@RequestParam(value = "correoe", required = true) String correoe) {

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO CONTENT");
	}

	/**
	 * Devuelve HTTP STATUS 200 si la contraseña es reinicializada correctamente
	 * para el usuario asociado al hashId.
	 * 
	 * @param hashId
	 * @return
	 */
	// @Deprecated
	@ApiOperation(value = AppPaths.Usuarios.RESET_PASS_PATH, nickname = "resetPassword", notes = "Se encarga de la modificación del usuario para el nuevo password")
	@GetMapping(value = "DeadEnd")
	public ResponseEntity<?> resetPassword_old(HttpServletRequest request,
			@RequestParam(value = "hashId", required = true) String hashId) {

		Response res = userService.resetPassword(hashId);

		if (res.getCode().equals(-1))// Verificar si el hashId existe, si no, no
										// hacer nada.
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No existe un vínculo válido para reinicializar contraseña ");

		if (res.getCode().equals(-2))// Verificar si el hashId ya fue usado
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El vínculo ya ha sido usado.");

		if (res.getCode().equals(-3))// Verificar si el hashId ya expiró
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El vínculo expiró.");

		Usuario usuario = userService.findOne(res.getCode());

		if (usuario == null)// Caso anómalo, problamente porque borraron el
							// usuario y el vínculo existe.
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No existe usuario asociado al vínculo de reinicialización de contraseña");

		// Ajustar un nuevo password, hacer que cambie clave, y ajustar la fecha
		// de último cambio de clave, resetear intentos
		// de login y enviar correo
		try {// Password por defecto es el ID.
			usuario.setPassword(UsuariosPasswordEncoder.encodePassword(usuario.getNumeroIdentificacion(),
					AppConstants.General.SALT));
		} catch (UsuariosException e) {
			String r = "Excepción codificando la constraseña por defecto luego de la reinicialización\n"
					.concat(e.getLocalizedMessage());
			e.printStackTrace();
			logger.error(r);
		}
		usuario.setCambioClave(true);
		usuario.setUltimoCambioClave(new Date());
		if (MasterSession.instance().getSessionCounterMap().containsKey(usuario.getLogin())) {
			MasterSession.instance().getSessionCounterMap().put(usuario.getLogin(), 0);
		}

		userService.save(usuario);
		String resString = "<html><head><meta http-equiv=\"refresh\" content=\"10;url="
				+ springEnv.getProperty("server.see_web_context") + "\" /></head>"
				+ "<body><div align=\"center\">Contraseña reinicializada para el usuario "
				+ "<strong>".concat(usuario.getLogin())
						.concat(".</strong>"
								+ "  Será <strong><font color=\"red\">redireccionado</font></strong> al inicio en segundos...</div></body>")
						.concat("</html>");
		logger.info(resString);
		return ResponseEntity.status(HttpStatus.OK).body(resString);
	}



}