package co.ias.usuarios.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.ias.common.entities.AuditAuthFallos;
import co.ias.usuarios.repositories.AuditAuthFallosRepository;
import co.ias.usuarios.services.AuditAuthFallosService;
@Component
public class AuditAuthFallosImpl implements AuditAuthFallosService {

	@Autowired
	private AuditAuthFallosRepository auditAuthFallosRepositopry;

	@Transactional(readOnly=false)
	@Override
	public void save(AuditAuthFallos auditAuthFallos) {
		auditAuthFallosRepositopry.save(auditAuthFallos);
	}

}
