package co.ias.perseo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ias.perseo.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}