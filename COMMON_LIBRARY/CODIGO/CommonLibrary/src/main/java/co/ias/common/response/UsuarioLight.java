package co.ias.common.response;

import java.util.List;

import co.ias.common.custom.PerfilAbstract;
import co.ias.common.entities.IpPermitida;
import co.ias.common.entities.TipoDocumento;
import co.ias.common.entities.UsuarioRolEntidad;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class UsuarioLight {

		private Integer id;
		private TipoDocumento tipoDocumento;
		private String numeroIdentificacion;
		private Integer dv;
		private String nombres;
		private String apellidos;
		private String email;
		private String login;		
		private PerfilAbstract perfil;
		private String estado;
		private List<IpPermitida> listIpPermitidas;
		private UsuarioRolEntidad usuarioRolEntidad;

}
