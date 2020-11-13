package co.common.custom;

import java.util.List;

import co.common.custom.MenuTemplate;

public class MenuTemplate {

	private String label;
	private Integer id;
	private Integer idPadre;
	private Integer idGrandParent;
	private Integer orden;
	private boolean isSelected;
	private String resource;
	private String tipoMenu;
	
	private List<MenuTemplate> children;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public List<MenuTemplate> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTemplate> children) {
		this.children = children;
	}
	
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getIdGrandParent() {
		return idGrandParent;
	}

	public void setIdGrandParent(Integer idGrandParent) {
		this.idGrandParent = idGrandParent;
	}

	public String getTipoMenu() {
		return tipoMenu;
	}

	public void setTipoMenu(String tipoMenu) {
		this.tipoMenu = tipoMenu;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "MenuTemplate [label=" + label + ", id=" + id + ", idPadre="
				+ idPadre + ", idGrandParent=" + idGrandParent + ", orden="
				+ orden + ", isSelected=" + isSelected + ", resource="
				+ resource + ", tipoMenu=" + tipoMenu + ", children="
				+ children + "]";
	}
	
	

}
