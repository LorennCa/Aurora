package co.perseo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import co.perseo.entities.ServiceGroup;


public interface ServiceGroupRepository extends JpaRepository<ServiceGroup, Integer> {
}