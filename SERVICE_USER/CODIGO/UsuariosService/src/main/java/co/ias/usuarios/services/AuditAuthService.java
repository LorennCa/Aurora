package co.ias.usuarios.services;

import co.ias.common.entities.AuditAuth;

public interface AuditAuthService {

	/**
	 * Guarda una entidad de auditoría AuditAuth
	 * 
	 * @param auditAuth
	 */
	public void save(AuditAuth auditAuth);

	/**
	 * Retorna un objeto AuditAuth encontrado por identificador único
	 * 
	 * @param id
	 * @return
	 */
	public AuditAuth findById(Integer id);

}
