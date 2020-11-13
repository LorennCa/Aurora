package co.odin.repositories;

import org.springframework.data.repository.CrudRepository;

import co.common.entities.AuditAuth;


public interface AuditAuthRepository extends CrudRepository<AuditAuth, Integer>{

}
