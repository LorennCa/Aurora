package co.odin.services;

import java.util.List;

import co.common.entities.DiccionarioClaves;
import co.odin.config.appconfig.ServiceRegistrator;


public interface PasswordDictionaryService extends ServiceRegistrator {

	/**
	 * retorna un Diccionario de claves dado como parámetro
	 * 
	 * @param Id
	 * @return
	 */
	public DiccionarioClaves findOne(Integer id);

	/**
	 * Guarda un Diccionario de claves de BD
	 * 
	 * @param diccionarioClaves
	 */
	public void save(DiccionarioClaves diccionarioClaves);

	/**
	 * Retorna el diccionario de claves
	 * 
	 * @return
	 */
	public List<DiccionarioClaves> findAll();

	/**
	 * Retorna verdadero si la clave está dentro de las palabras restringidas de
	 * lo cantrario, NO CONTENT
	 * 
	 * @param clave
	 * @return
	 */
	public boolean inRestrictedWords(String clave);

}
