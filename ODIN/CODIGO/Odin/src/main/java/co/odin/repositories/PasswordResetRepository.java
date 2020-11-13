package co.odin.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import co.common.entities.DynamicLink;


public interface PasswordResetRepository extends CrudRepository<DynamicLink, Integer> {
	
	@Query(value = "SELECT * FROM dynamic_links WHERE confirmation_hash = :hashId", nativeQuery = true)
	public DynamicLink findByConfirmation_hash(@Param("hashId") String hashId);

	@Query(value = "SELECT * FROM dynamic_links WHERE user_login = :user_login", nativeQuery = true)
	public DynamicLink findByUserLogin(@Param("user_login") String user_login);

	@Modifying
    @Transactional
    @Query("delete from DynamicLink din where din.user_login = :user_login")
	public int deleteByUserLogin(@Param("user_login") String user_login);
}