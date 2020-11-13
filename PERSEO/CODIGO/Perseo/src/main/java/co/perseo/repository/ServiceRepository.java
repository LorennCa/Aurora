package co.perseo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.perseo.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
	
	@Query(value = "SELECT service.*"
			     + "FROM sr_services service "
			     + "INNER JOIN sr_applications application ON (service.parent_app_id = application.application_id) "
			     + "WHERE application.application_id = :appId", nativeQuery = true)
	public List<Service> listServicesByApplicationId(@Param("appId")String appId);
}