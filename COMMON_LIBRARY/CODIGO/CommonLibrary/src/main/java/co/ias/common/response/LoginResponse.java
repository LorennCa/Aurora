package co.ias.common.response;

import java.util.Date;

import co.ias.common.custom.MenuTemplate;
import co.ias.common.entities.UsuarioRolEntidad;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class LoginResponse extends Response {
	private String userFullName;
	private Date lastLogin;
	private String scope;
	private Integer id;
	private String login;
	private boolean cambioClave;
	private String uriValidarClaveHistorico;
	private String uriValidarClaveRestringido;
	private String uriGuardarClave;
	private String uriValidarUsuarioDocumentoEntidad;
	private String uriValidarPerfilEliminar;
	private String uriValidaToken;
	private String uriLogout;
	private String uriOperaciones;
	private String uriValidarFecha;
	private String IP;
	private UsuarioRolEntidad usuarioRolEntidad;
	private MenuTemplate menuTemplate;
	private Integer perfilId;
	private OtrasOpciones otrasOpciones;

}