package co.ias.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;

import co.ias.common.entities.AuditAuth;


public interface AuditAuthRepository extends CrudRepository<AuditAuth, Integer>{

}
