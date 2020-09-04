package co.ias.usuarios.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.entities.DiccionarioClaves;
import co.ias.usuarios.repositories.PasswordDictionaryRepository;
import co.ias.usuarios.services.PasswordDictionaryService;

@Component
public class PasswordDictionaryServiceImpl implements PasswordDictionaryService {

	@Autowired
	private PasswordDictionaryRepository diccionarioClavesRepository;

	@Override
	public DiccionarioClaves findOne(Integer id) {
		return diccionarioClavesRepository.findOne(id);
	}

	@Override
	public void save(DiccionarioClaves diccionarioClaves) {
		// Validaciones para la cadena de claves
		if (diccionarioClaves.getCadenaClaves().length() != 0) {
			diccionarioClaves.setCadenaClaves(diccionarioClaves.getCadenaClaves().trim());
		}
		diccionarioClavesRepository.save(diccionarioClaves);
	}

	@Override
	public List<DiccionarioClaves> findAll() {
		return (List<DiccionarioClaves>) diccionarioClavesRepository.findAll();
	}

	@Override
	public boolean inRestrictedWords(String clave) {
		List<DiccionarioClaves> list = (List<DiccionarioClaves>) diccionarioClavesRepository.findAll();
		String stringComplete = list.get(0).getCadenaClaves();
		
		if (stringComplete != null && stringComplete.length() > 0) {
			String[] words = stringComplete.split(";");
			for (int i = 0; i < words.length; i++) {
				if (clave.toLowerCase().contains(words[i].toLowerCase()))
					return true;
			}
		}
		return false;
	}
}