package co.ias.usuarios.services.impl;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.entities.Menu;
import co.ias.common.entities.Perfil;
import co.ias.usuarios.repositories.MenuOwnRepository;
import co.ias.usuarios.repositories.MenuRepository;
import co.ias.usuarios.services.MenuService;
import co.ias.usuarios.utils.AppConstants;

@Component
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MenuOwnRepository menuOwnRepository;

	@Override
	public List<Menu> findAll() {
		return (List<Menu>) menuRepository.findAll();
	}

	@Override
	public List<Menu> findByTipoMenuNot(String tipoMenu) {
		return menuOwnRepository.findByTipoMenuNot(tipoMenu);
	}

	public List<Menu> findByTipoMenu(String tipoMenu) {
		return menuOwnRepository.findByTipoMenu(tipoMenu);
	}

	@Override
	public Menu findOne(Integer id) {
		return menuRepository.findOne(id);
	}

	@Override
	public List<Menu> findUriServicioContainingTipoMenuOrNivel(String uri, String tipoMenu, Integer nivel) {
		return menuOwnRepository.findUriServicioContainingTipoMenuOrNivel(uri, tipoMenu, nivel);
	}

	@Override
	public List<Menu> findByIdIn(Set<Integer> ids) {
		return menuOwnRepository.findByIdIn(ids);
	}

	@Override
	public void insertNewMenuToPerfil(Perfil perfil, List<Menu> menuList) {

		// Recorre los Menús encontrados:
		for (Menu menuFound : menuList) {

			if (null != menuFound) {

				/**
				 * Si no se ha ingresado en las opcionesdeMenu del perfil,
				 * valida
				 */

				if (!perfil.getOpcionesMenu().stream().anyMatch(m -> m.getId().equals(menuFound.getId()))) {

					/*
					 * Si el menú es nivel III (nieto), busca 2 niveles más
					 * arriba (Padre y abuelo)
					 */
					if (menuFound.getNivel() == AppConstants.General.MENU_NIVEL_TRES) {

						// Se adiciona el menuNivelIII a la lista
						menuFound.getPerfiles().add(perfil);
						perfil.getOpcionesMenu().add(menuFound);


						if (menuFound.getMenuPadre() != null && menuFound.getMenuPadre().getId() != null) {

							// Busca si NO está agregado el nivelII

							if (!perfil.getOpcionesMenu().stream()
									.anyMatch(m -> m.getId().equals(menuFound.getMenuPadre().getId()))) {

								// Busca el nivel II
								Menu menuNivelII = findOne(menuFound.getMenuPadre().getId());

								// Con el Menú de nivel II busco su padre
								if (menuNivelII != null) {

									// Se adiciona el menuNvelII a la lista
									menuNivelII.getPerfiles().add(perfil);
									perfil.getOpcionesMenu().add(menuNivelII);

									if (menuNivelII.getMenuPadre() != null
											&& menuNivelII.getMenuPadre().getId() != null) {

										// Busca el nivel I
										Menu menuNivelI = findOne(menuNivelII.getMenuPadre().getId());

										if (menuNivelI != null) {

											// Busca si NO está agregado el
											// nivelI

											if (!perfil.getOpcionesMenu().stream()
													.anyMatch(m -> m.getId().equals(menuNivelI.getId()))) {

												// Se adiciona el menuNivelI a
												// lista
												menuNivelI.getPerfiles().add(perfil);
												perfil.getOpcionesMenu().add(menuNivelI);
											}
										}
									}
								}
							}
						}
					} else if (menuFound.getNivel() == AppConstants.General.MENU_NIVEL_DOS) {

						// Se adiciona el menuNivelII a la lista
						menuFound.getPerfiles().add(perfil);
						perfil.getOpcionesMenu().add(menuFound);

						if (menuFound.getMenuPadre() != null && menuFound.getMenuPadre().getId() != null) {

							// Busca si NO está agregado el nivelI

							if (!perfil.getOpcionesMenu().stream()
									.anyMatch(m -> m.getId().equals(menuFound.getMenuPadre().getId()))) {

								// Busca el nivel I
								Menu menuNivelI = findOne(menuFound.getMenuPadre().getId());

								if (menuNivelI != null) {
									// Se adiciona el menuNivelII a la lista
									menuNivelI.getPerfiles().add(perfil);
									perfil.getOpcionesMenu().add(menuNivelI);

								}

							}

						}

					} else if (menuFound.getNivel() == AppConstants.General.MENU_NIVEL_UNO) {
						// Se adiciona el menuNivelI a la
						menuFound.getPerfiles().add(perfil);
						perfil.getOpcionesMenu().add(menuFound);
					}
				}
			}
		}
	}

	@Override
	public List<Menu> findByMenuPadre(Menu menuPadre) {
		return menuOwnRepository.findByMenuPadre(menuPadre);
	}
}