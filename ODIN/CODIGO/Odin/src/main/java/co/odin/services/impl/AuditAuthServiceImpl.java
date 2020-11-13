package co.odin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.common.entities.AuditAuth;
import co.odin.repositories.AuditAuthRepository;
import co.odin.services.AuditAuthService;

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
