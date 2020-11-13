package co.odin.services.synchronize;


import co.common.entities.Perfil;
import co.common.entities.Usuario;


public interface UsuariosSynchronizeService {

	public Usuario saveUser(Usuario usuario) throws Exception;

	public Usuario saveUserMod(Usuario usuarioFound) throws Exception;

	public 	Usuario saveUserNew(Usuario usuario) throws Exception;

	public void savePerfil(Perfil perfil);

	public void updateProfile(Perfil perfil);

	public void deletePerfil(Integer id);
	
}
