package co.odin.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.common.entities.Entidad;
import co.odin.repositories.EntidadOwnRepository;
import co.odin.repositories.EntidadRepository;
import co.odin.services.EntidadService;
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

//	@Override
//	public List<Entidad> findByEmisorTrue() {
//		return entidadOwnRepository.findByEmisorTrue();
//	}

//	@Override
//	public List<Entidad> findByAfiliadoTrue() {
//		return entidadOwnRepository.findByAfiliadoTrue();
//	}

//	@Override
//	public List<Entidad> findByScbTrue() {
//		return entidadOwnRepository.findByScbTrue();
//	}

	@Override
	public List<Entidad> findBySuperAdminTrue() {
		return entidadOwnRepository.findBySuperAdminTrue();
	}

	
}