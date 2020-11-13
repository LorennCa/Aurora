package co.odin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.common.entities.AuditAuthFallos;
import co.odin.repositories.AuditAuthFallosRepository;
import co.odin.services.AuditAuthFallosService;
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
