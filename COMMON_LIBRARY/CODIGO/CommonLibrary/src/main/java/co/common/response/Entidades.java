package co.common.response;

import java.util.List;

import co.common.entities.Entidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Entidades {

	private List<Entidad> scb;

	private List<Entidad> emisores;

	private List<Entidad> superAdministrador;

	private List<Entidad> afiliados;

}
