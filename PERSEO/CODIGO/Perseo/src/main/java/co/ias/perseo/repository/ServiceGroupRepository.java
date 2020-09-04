package co.ias.perseo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import co.ias.perseo.entities.ServiceGroup;


public interface ServiceGroupRepository extends JpaRepository<ServiceGroup, Integer> {
}