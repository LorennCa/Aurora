package co.ias.common.wrappers;

import java.util.List;

import co.ias.common.entities.Entidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EntidadesParticipantes {

	private List<Entidad> mec;
	private List<Entidad> scb;
	private List<Entidad> emisores;

	
}
