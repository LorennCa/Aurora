package co.ias.common.wrappers;


import com.fasterxml.jackson.annotation.JsonBackReference;

import co.ias.common.entities.TipoDocumento;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioWrapper {
	
	@JsonBackReference
	private TipoDocumento tipoDocumento;
	private String numeroDocumento;
	private String cuentaDeceval;
	private Integer operacion;
	private Integer idBusqueda;
	private boolean byOperation;

	

}
