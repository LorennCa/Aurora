package co.ias.usuarios.services.synchronize;


import co.ias.common.entities.Perfil;
import co.ias.common.entities.Usuario;


public interface UsuariosSynchronizeService {

	public Usuario saveUser(Usuario usuario) throws Exception;

	public Usuario saveUserMod(Usuario usuarioFound) throws Exception;

	public 	Usuario saveUserNew(Usuario usuario) throws Exception;

	public void savePerfil(Perfil perfil);

	public void updateProfile(Perfil perfil);

	public void deletePerfil(Integer id);
	
}
