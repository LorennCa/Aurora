package co.odin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import co.common.custom.MenuTemplate;
import co.common.entities.IpPermitida;
import co.common.entities.Menu;
import co.common.entities.Perfil;
import co.common.response.ResourceResponse;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.config.appconfig.WebContextConfiguration;
import co.odin.enums.TipoMenuEnum;


public class UsuariosUtil {

	
	/**
	 * logger de applicación
	 */
	private static final Logger logger = LogManager.getLogger(UsuariosUtil.class);
	


	
	@SuppressWarnings("unused")
	private static WebContextConfiguration serverconf;
	
	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private static MessageSource mrc = MessageSourceConfig.messageSource();

	/**
	 * Lista de tipo Menu donde se van a almacenar los permisos que se tienen
	 * sobre otros perfiles
	 */
	private static List<Menu> permisosPerfilparaPerfil = new ArrayList<Menu>();

	//private static boolean configured = false;

	/**
	 * Devuelve null si existió algún problema, de lo contrario, una respuesta con un correo electrónico 
	 * correcto y completo tipo MIME.
	 * @param request 
	 * @param fromEmail
	 * @param toEmail
	 * @param message
	 * @param subject
	 * @return
	 */



	/**
	 * Encargado de retornar la IP de una petición, dado un Request que entra
	 * como parámetro.
	 * 
	 * @param request
	 * @return
	 */
	public static String getIPFromRequest(HttpServletRequest request) {
		String IP = request.getHeader("X-Forwarded-For");
		if (null == IP || IP.length() == 0) {
			IP = request.getRemoteAddr();
		}
		logger.info(mrc.getMessage(
				"odin.security.autenticacion.request.IP.info", null, null) + IP);

		return IP;
	}

	/**
	 * Utilería encargada de recibir una lista de Strings y ajustarla a una
	 * cadena separada por comas
	 * 
	 * @param listoToconvert
	 * @return
	 */
	public static String ListToString(List<String> listoToconvert) {
		StringBuilder stringBuilder = new StringBuilder();
		if (listoToconvert != null && listoToconvert.size() != 0) {
			for (int i = 0; i < listoToconvert.size(); i++) {
				stringBuilder.append(listoToconvert.get(i));
				if (i != listoToconvert.size() - 1)
					stringBuilder.append(" ");
			}
			return stringBuilder.toString();
		}
		return "";
	}

//	public static String list(List<TipoFormaPago> formaPagoList) {
//
//		List<String> slist = new ArrayList<String>();
//		for (TipoFormaPago tipoFormaPago : formaPagoList) {
//			slist.add(tipoFormaPago.getNombre());
//		}
//
//		return StringUtils.join(slist, ',');
//	}

	/**
	 * Recupera todas las opciones de Menú de un perfil en específico
	 * 
	 * @param opcionesMenu
	 * @return
	 */
	public static MenuTemplate construirMenuGeneral(List<Menu> opcionesMenu) {
		MenuTemplate rootNode = new MenuTemplate();
		List<MenuTemplate> parentsNodes = new ArrayList<MenuTemplate>();

		for (Menu menu : opcionesMenu) {
			if (menu.getNivel() != null
					&& menu.getNivel() == AppConstants.General.MENU_NIVEL_ROOT) {
				rootNode.setLabel(menu.getEtiqueta());
			}
			if (menu.getNivel() != null
					&& menu.getNivel() == AppConstants.General.MENU_NIVEL_UNO
					&& menu.getTipoMenu().equalsIgnoreCase(
							TipoMenuEnum.GENERAL.name())) {
				MenuTemplate parentNode = asignarValoresMenuToMenuTemplate(
						menu, opcionesMenu, null);
				parentNode
						.setChildren(seekChildren(opcionesMenu, menu.getId()));
				parentsNodes.add(parentNode);
			}

			if (menu.getNivel() == AppConstants.General.MENU_NIVEL_TRES
					&& menu.getMenuPadre().getEtiqueta().toLowerCase()
							.contains(AppConstants.General.CADENA_PERFIL)) {
				permisosPerfilparaPerfil.add(menu);
			}
		}
		parentsNodes = ordenarListaMenuTemplate(parentsNodes);
		rootNode.setChildren(parentsNodes);
		return rootNode;
	}

	/**
	 * Construye TODAS laa opciones de Menú (MenuTemplate) para un conjunto de
	 * opciones de Menú de perfil. Es usado cuando se va a actualizar un perfil
	 * y se necesita TODAS las opciones de Menú así el Perfil no las tenga
	 * 
	 * @param todasOpcionesMenu
	 * @param opcionesMenuPerfil
	 * @return
	 */
	public static MenuTemplate construirMenuToUpdateOrCreatePerfil(
			List<Menu> todosMenus, List<Menu> menusPerfil) {
		MenuTemplate rootNode = new MenuTemplate();
		List<MenuTemplate> parentsNodes = new ArrayList<MenuTemplate>();
		for (Menu menu : todosMenus) {
			if (menu.getNivel() != null
					&& menu.getNivel() == AppConstants.General.MENU_NIVEL_ROOT) {
				rootNode.setLabel(menu.getEtiqueta());
			}
			if (menu.getNivel() != null
					&& menu.getNivel() == AppConstants.General.MENU_NIVEL_UNO) {
				MenuTemplate parentNode = asignarValoresMenuToMenuTemplate(
						menu, menusPerfil, null);
				parentNode.setChildren(seekChildrenPerfilToUpdate(todosMenus,
						menusPerfil, menu.getId()));
				parentsNodes.add(parentNode);
			}
		}
		parentsNodes = ordenarListaMenuTemplate(parentsNodes);
		rootNode.setChildren(parentsNodes);
		return rootNode;
	}

	/**
	 * Busca los nodos hijos dado un nodo padre
	 * 
	 * @param opcionesMenu
	 * @param parentId
	 * @return
	 */
	public static List<MenuTemplate> seekChildren(List<Menu> opcionesMenu,
			Integer parentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		for (Menu opcionMenu : opcionesMenu) {
			if (opcionMenu.getMenuPadre() != null
					&& opcionMenu.getMenuPadre().getId().compareTo(parentId) == 0
					&& opcionMenu.getNivel() == AppConstants.General.MENU_NIVEL_DOS) {
				MenuTemplate child = new MenuTemplate();
				child.setLabel(opcionMenu.getEtiqueta());
				child.setId(opcionMenu.getId());
				child.setIdPadre(opcionMenu.getMenuPadre().getId());
				child.setOrden(opcionMenu.getOrden());
				child.setIsSelected(AppConstants.General.ESTADO_HABILITADO);
				child.setResource((opcionMenu.getUriServicio()));
				children.add(child);
			}
		}
		children = ordenarListaMenuTemplate(children);
		return children;
	}

	/**
	 * Busca los nodos hijos de un menu padre. Se especializa en agregar todos
	 * los hijos, pero en caso de no tener la opción de Menú en BD, se pone el
	 * estado de Menu en false
	 * 
	 * @param opcionesMenu
	 * @param parentId
	 * @return
	 */
	public static List<MenuTemplate> seekChildrenPerfilToUpdate(
			List<Menu> todosMenus, List<Menu> menusPerfil, Integer parentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		// Recorre todas las opciones de Menú
		for (Menu menu : todosMenus) {

			// Filro por nivel de profundidad 2 (Hijos) Ó si se trata de un
			// menú de botones (todo de Parametrización o Gestión de Demandas)
			if (menu.getNivel() != null
					&& menu.getNivel().compareTo(
							AppConstants.General.MENU_NIVEL_DOS) == 0
					|| (menu.getTipoMenu()
							.equalsIgnoreCase(TipoMenuEnum.ESPECIFICO.name()))) {

				if (menu.getMenuPadre() != null
						&& menu.getMenuPadre().getId().compareTo(parentId) == 0) {

					MenuTemplate child = asignarValoresMenuToMenuTemplate(menu,
							menusPerfil, null);
					children.add(child);

					/*
					 * Busca los nietos (Nivel 3), le pasa el parentId que
					 * corresponde al grandParentId
					 */

					child.setChildren(seekNietos(todosMenus, menusPerfil,
							menu.getId(), parentId));
				}
			}
		}
		children = ordenarListaMenuTemplate(children);
		return children;
	}

	/**
	 * Busca los nodos hijos de un menu Hijo. Se especializa en agregar todos
	 * los nietos, pero en caso de no tener la opción de Menú en BD, se pone el
	 * estado de Menu en false
	 * 
	 * @param todosMenus
	 * @param menusPerfil
	 * @param parentId
	 * @param nivel
	 * @return
	 */
	public static List<MenuTemplate> seekNietos(List<Menu> todosMenus,
			List<Menu> menusPerfil, Integer parentId, Integer grandParentId) {
		List<MenuTemplate> children = new ArrayList<MenuTemplate>();

		// Recorre todas las opciones de Menú
		for (Menu menu : todosMenus) {

			// Filro por nivel de profundidad 3 (Nietos)
			if (menu.getNivel() != null
					&& menu.getNivel().compareTo(
							AppConstants.General.MENU_NIVEL_TRES) == 0) {

				// Adiciono únicamente a los hijos del parámetro id Padre
				if (menu.getMenuPadre().getId() != null
						&& menu.getMenuPadre().getId().compareTo(parentId) == 0) {
					MenuTemplate child = asignarValoresMenuToMenuTemplate(menu,
							menusPerfil, grandParentId);
					children.add(child);
				}
			}
		}
		return children;
	}

	/**
	 * Asigna los valores de Menu a MenuTemplate (Formato quie sirve a la vista)
	 * Si el flag 'Asignar todo' es true, no busca en la lista de opciones del
	 * perfil para cambiar su estado en true
	 * 
	 * @param menu
	 * @param menuTemplate
	 * @param menusPerfil
	 * @param listaAAgregar
	 * @param asignar
	 *            todo
	 * @return
	 */
	public static MenuTemplate asignarValoresMenuToMenuTemplate(Menu menu,
			List<Menu> menusPerfil, Integer grandParentId) {

		MenuTemplate menuTemplate = new MenuTemplate();
		menuTemplate.setLabel(menu.getEtiqueta());
		menuTemplate.setId(menu.getId());

		// Establece el idGrandParent
		menuTemplate.setIdGrandParent(grandParentId);

		menuTemplate.setIdPadre(menu.getMenuPadre().getId());
		menuTemplate.setOrden(menu.getOrden());
		menuTemplate.setTipoMenu(menu.getTipoMenu());
		menuTemplate.setIsSelected(AppConstants.General.ESTADO_DESHABILITADO);

		/*
		 * Si viene vacia la lista de menusPerfil, quiero que se arme el
		 * MenuTemplate con TODAS las opciones de Menú, sin hacer una búsqueda
		 * en los menusPerfil
		 */
		if (menusPerfil != null && menusPerfil.size() != 0) {
			if (existsInList(menusPerfil, menu.getId())) {
				menuTemplate
						.setIsSelected(AppConstants.General.ESTADO_HABILITADO);
			}
		}

		menuTemplate.setResource((menu.getUriServicio()));
		return menuTemplate;
	}

	/**
	 * Construye los permisos que tiene un perfil para administrar los demás
	 * perfiles: Crear, consultar, modificar, eliminar
	 * 
	 * @param todasOpcionesMenu
	 * @param opcionesMenuPerfil
	 * @return
	 */
	public static List<MenuTemplate> construirPermisosPerfilParaPerfil(
			List<Menu> todasOpcionesMenu, List<Menu> opcionesMenuPerfil) {
		List<Menu> todosPermisosPeriles = new ArrayList<Menu>();
		List<MenuTemplate> menuTemplatePermisos = new ArrayList<MenuTemplate>();

		// Obtengo únicamente TODOS los permisos CRUD para perfiles
		for (Menu menu : todasOpcionesMenu) {
			if (menu.getNivel() == AppConstants.General.MENU_NIVEL_TRES
					&& menu.getMenuPadre().getEtiqueta().toLowerCase()
							.contains(AppConstants.General.CADENA_PERFIL)) {
				todosPermisosPeriles.add(menu);
			}
		}

		// primero ordeno para armar el menuTemplate con el CRUD que necesita
		// FRONT
		todosPermisosPeriles = ordenarListaMenu(todosPermisosPeriles);

		// Se recorre la lista de TODOS los permisos CRUD
		for (Menu menu : todosPermisosPeriles) {

			MenuTemplate mtPermiso = asignarValoresMenuToMenuTemplate(menu,
					opcionesMenuPerfil, null);
			menuTemplatePermisos.add(mtPermiso);
		}
		return menuTemplatePermisos;
	}

	/**
	 * Ordena de manera ascendente dado un listado de tipo MenuTemplate
	 * 
	 * @param list
	 * @return
	 */
	public static List<MenuTemplate> ordenarListaMenuTemplate(
			List<MenuTemplate> list) {
		Collections.sort(list, new Comparator<MenuTemplate>() {
			@Override
			public int compare(MenuTemplate mT1, MenuTemplate mT2) {
				return mT1.getOrden().compareTo(mT2.getOrden());
			}
		});
		return list;
	}

	/**
	 * Ordena de manera ascendente las listas de tipo Menu a través de su campo
	 * orden
	 * 
	 * @param list
	 * @return
	 */
	public static List<Menu> ordenarListaMenu(List<Menu> list) {
		Collections.sort(list, new Comparator<Menu>() {
			@Override
			public int compare(Menu menu1, Menu menu2) {
				return menu1.getOrden().compareTo(menu2.getOrden());
			}
		});
		return list;
	}

	/**
	 * Busca en listado por identificador
	 * 
	 * @param id
	 */
	public static boolean existsInList(List<Menu> lista, Integer id) {
		boolean encontrado = false;
		for (int i = 0; i < lista.size() && !encontrado; i++) {
			if (lista.get(i).getId().intValue() == id) {
				encontrado = true;
			}
		}
		return encontrado;
	}

	/**
	 * Busca en listado por identificador
	 * 
	 * @param id
	 */
	public static boolean existsInList(List<Perfil> lista, Perfil perfil) {
		if (lista.indexOf(perfil) != -1) {
			return true;
		}
		return false;
	}

	/*
	 * Busca en listado por String
	 * 
	 * @param id
	 */
	public static boolean existsInList(List<IpPermitida> lista, String cadena) {
		boolean encontrado = false;
		for (int i = 0; i < lista.size() && !encontrado; i++) {

			if (lista.get(i) != null && lista.get(i).getIp() != null
					&& lista.get(i).getIp().trim().equalsIgnoreCase(cadena)) {
				encontrado = true;
			}
		}
		return encontrado;
	}

	/**
	 * Método encargado de ajustar una respuesta genérica exitosa de la
	 * operación
	 */
	public static void successAccessResponse() {
		ResourceResponse.getSingleInstance().setCode(
				org.apache.http.HttpStatus.SC_OK);
		ResourceResponse.getSingleInstance().setMessage(
				mrc.getMessage("odin.security.autorizacion.resourceAccess.OK",
						null, null));

	}

	/**
	 * Ajusta una respuesta genérica de eliminación de un recurso
	 */
	public static void successDeleteResponse() {
		ResourceResponse.getSingleInstance().setCode(
				org.apache.http.HttpStatus.SC_NO_CONTENT);
		ResourceResponse
				.getSingleInstance()
				.setMessage(
						mrc.getMessage(
								"odin.security.autorizacion.resourceAccess.DELETE.success",
								null, null));
	}

	/**
	 * Ajusta respuesta global de un recurso no encontrado
	 */
	public static void resourceNotFound() {
		ResourceResponse.getSingleInstance().setCode(
				org.apache.http.HttpStatus.SC_NO_CONTENT);
		ResourceResponse.getSingleInstance().setMessage(
				mrc.getMessage(
						"odin.security.autorizacion.resourceAccess.NOT_FOUND",
						null, null));
	}

	/**
	 * Convierte las fechas (Date) y la hora (String) en un formato que entra
	 * como parámetro
	 * 
	 * @param date
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String parseDateTimeToString(Date date, String time,
			String pattern) {

		try {

			SimpleDateFormat format = new SimpleDateFormat(pattern);

			if (date != null) {
				return format.format(date);
			} else {
				if (time != null && time.length() >= 15) {
					time = time.substring(11, 19);
					return time;
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			return "pailas" + e.getMessage();
		}

	}

	/**
	 * Concatena la fecha y hora por separado para retornar un objeto Date
	 * 
	 * @param fecha
	 * @param hour
	 * @return
	 */
	public static Calendar concatDateTime(Date fecha, String hourUnFormatted) {

		String hour = UsuariosUtil.parseDateTimeToString(null, hourUnFormatted,
				AppConstants.General.HOUR_PATTERN);

		String date = UsuariosUtil.parseDateTimeToString(fecha, null,
				AppConstants.General.DATE_PATTERN);

		StringBuilder dateTimeBuilder = new StringBuilder();
		dateTimeBuilder.append(date).append(" ").append(hour);

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				AppConstants.General.DATE_TIME_PATTERN);

		try {
			calendar.setTime(sdf.parse(dateTimeBuilder.toString()));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return calendar;
	}

	public static String getOS() {
		String os = System.getProperty("os.name");

		String name = "";

		if (os.startsWith("win") || os.startsWith("Win") || os.startsWith("WIN")) {
			name = AppConstants.General.OS_WINDOWS_NAME;
		} else {
			name = AppConstants.General.OS_UNIX_NAME;
		}
		return name;
	}



	public static void main(String[] args) {
		
	}
}
