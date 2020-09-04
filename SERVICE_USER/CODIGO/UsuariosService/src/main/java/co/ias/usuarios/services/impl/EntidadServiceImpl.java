package co.ias.usuarios.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.ias.common.entities.Entidad;
import co.ias.usuarios.repositories.EntidadOwnRepository;
import co.ias.usuarios.repositories.EntidadRepository;
import co.ias.usuarios.services.EntidadService;
@Component
public class EntidadServiceImpl implements EntidadService {

	@Autowired
	private EntidadRepository entidadRepository;

	@Autowired
	private EntidadOwnRepository entidadOwnRepository;

	@Override
	public List<Entidad> findAll() {
		return (List<Entidad>) entidadRepository.findAll();
	}

	@Override
	public Entidad findOne(Integer id) {
		return entidadRepository.findOne(id);
	}

	@Override
	public List<Entidad> findByEmisorTrue() {
		return entidadOwnRepository.findByEmisorTrue();
	}

	@Override
	public List<Entidad> findBySuperAdminTrue() {
		return entidadOwnRepository.findBySuperAdminTrue();
	}
}