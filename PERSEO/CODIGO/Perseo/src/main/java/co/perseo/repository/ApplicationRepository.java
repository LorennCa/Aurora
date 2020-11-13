package co.perseo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.perseo.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}