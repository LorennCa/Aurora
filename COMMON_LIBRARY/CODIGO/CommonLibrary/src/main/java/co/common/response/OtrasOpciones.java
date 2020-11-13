package co.common.response;

import java.util.HashMap;
import java.util.List;

import co.common.custom.MenuTemplate;
import co.common.entities.Carrito;
import co.common.entities.Compras;
import co.common.entities.IndicadorTitulo;
import co.common.entities.Inventario;
import co.common.entities.Perfil;
import co.common.entities.Proveedor;
import co.common.entities.TipoDocumento;
import co.common.entities.Usuario;
import co.common.wrappers.EntidadesParticipantes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString 
public class OtrasOpciones {

	private Integer businessErrorCode;
	private MenuTemplate menuOpciones;
	private List<Perfil> listaPerfil;
	private List<MenuTemplate> permisos;
	private PerfilResponse perfil;
	private List<UsuarioLight> listaUsuarios;
	private Entidades entidades;
	private Usuario usuario;
	private List<TipoDocumento> tipoDocumento;
	private String diccionario;
	private List<IndicadorTitulo> tipoIndTitular;
	private EntidadesParticipantes entidadesParticipantes;
	private List<String> listaSizeOperaciones;
	private long fechaServidor;
	private String repuestaOk;
	private String listaPreciosR;
	private HashMap<String, Integer> autorizaciones;
	private List<Inventario> inventario;
	private List<Carrito> carrito;
	private List<Proveedor> proveedor;	
	private List<Compras> compras;
	private Inventario detalles;
}
