package co.common.wrappers;

import co.common.entities.TipoDocumento;
import co.common.entities.UsuarioRolEntidad;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class UsuarioEntidadWrapper {
	
	private Integer id;
	private String numeroDocumento;
	private TipoDocumento tipoDocumento;
	private UsuarioRolEntidad usuarioRolEntidad;
	
	
	

}
