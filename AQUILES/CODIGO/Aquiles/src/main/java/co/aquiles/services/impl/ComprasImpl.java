package co.aquiles.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.aquiles.repositories.ComprasRepository;
import co.aquiles.services.ComprasService;
import co.common.entities.Compras;

@Component
public class ComprasImpl implements ComprasService{
	
	@Autowired
	private ComprasRepository comprasRepository;
	
	@Override
	public List<Compras> retornaCompras(){
		return (List<Compras>) comprasRepository.findAll();
	}

}
