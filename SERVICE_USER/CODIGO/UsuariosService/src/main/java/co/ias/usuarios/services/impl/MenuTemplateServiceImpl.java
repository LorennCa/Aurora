package co.ias.usuarios.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.Menu;
import co.ias.common.entities.Perfil;
import co.ias.usuarios.enums.TipoMenuEnum;
import co.ias.usuarios.services.MenuService;
import co.ias.usuarios.services.MenuTemplateService;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.UsuariosUtil;

@Component
public class MenuTemplateServiceImpl implements MenuTemplateService {

	@Autowired
	private MenuService menuService;

	@Override
	public List<MenuTemplate> getPermisosByPerfil(Perfil perfil, String uri) {

		List<MenuTemplate> menuTemplateList = new ArrayList<MenuTemplate>();

		List<Menu> menusNivelTres = menuService
				.findUriServicioContainingTipoMenuOrNivel(uri,
						TipoMenuEnum.ESPECIFICO.name(),
						AppConstants.General.MENU_NIVEL_TRES);

		// Obtengo el menuPadre para hallar el grandParent
		Menu menuPadre = menuService.findOne(menusNivelTres.get(0).getMenuPadre().getId());

		for (Menu menu : menusNivelTres) {

			MenuTemplate mtPermiso = UsuariosUtil.asignarValoresMenuToMenuTemplate(
					menu, perfil.getOpcionesMenu(), menuPadre.getMenuPadre()
							.getId());
			menuTemplateList.add(mtPermiso);
		}

		// primero ordeno para armar el menuTemplate con el CRUD to FRONT
		menuTemplateList = UsuariosUtil.ordenarListaMenuTemplate(menuTemplateList);

		return menuTemplateList;
	}

}