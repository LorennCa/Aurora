package co.ias.usuarios.services.synchronize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.ias.common.entities.Usuario;
import co.ias.usuarios.repositories.UserRepository;
import co.ias.usuarios.services.PasswordResetService;

@Transactional
@Component
public class UsuariosTransactionalService {


	/**
	 * Inyección de dependencias
	 */
	@Autowired
	private UserRepository usuarioRepository;
	
	@Autowired
	private PasswordResetService prService;
	

	public Usuario saveUser(Usuario usuario) {
		
		return usuarioRepository.save(usuario);

	}

	public Usuario saveUserMod(Usuario usuario) {
		
			prService.deletePasswordReset(usuario.getLogin());
			return saveUser(usuario);
		
		
	}
	
	
	

}
