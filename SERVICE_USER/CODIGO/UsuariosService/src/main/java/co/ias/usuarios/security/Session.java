package co.ias.usuarios.security;

import java.util.Date;

import co.ias.common.entities.Usuario;
import co.ias.common.response.OAuthResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Session {
	private Usuario usuario;
	private Date dateTimeSession;
	private OAuthResponse token;
	private String ip;
	private Integer idAudit;
}