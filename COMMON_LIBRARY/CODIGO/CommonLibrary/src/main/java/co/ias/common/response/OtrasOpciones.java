package co.ias.common.response;

import java.util.HashMap;
import java.util.List;

import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.Carrito;
import co.ias.common.entities.IndicadorTitulo;
import co.ias.common.entities.Inventario;
import co.ias.common.entities.Perfil;
import co.ias.common.entities.TipoDocumento;
import co.ias.common.entities.Usuario;
import co.ias.common.wrappers.EntidadesParticipantes;
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
	
}
