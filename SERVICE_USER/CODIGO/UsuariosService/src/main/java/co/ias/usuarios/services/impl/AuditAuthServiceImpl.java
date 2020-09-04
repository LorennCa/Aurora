package co.ias.usuarios.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.ias.common.entities.AuditAuth;
import co.ias.usuarios.repositories.AuditAuthRepository;
import co.ias.usuarios.services.AuditAuthService;

@Component
public class AuditAuthServiceImpl implements AuditAuthService{

	@Autowired
	private AuditAuthRepository auditAuthRepository;

	@Transactional(readOnly=false)
	@Override
	public void save(AuditAuth auditAuth) {
		auditAuthRepository.save(auditAuth);
	}

	@Transactional(readOnly=true)
	@Override
	public AuditAuth findById(Integer id) {
		return auditAuthRepository.findOne(id);
	}
	
	
}
