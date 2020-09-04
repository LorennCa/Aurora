package co.ias.teseo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import co.ias.teseo.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	@Query(value = "SELECT user.* "
				 + "FROM auth_app_user user "			
			     + "WHERE " + "user.app_user_name = :username", nativeQuery = true)
	public User findByUser(@Param("username") String username);

	@Modifying
    @Transactional
    @Query("delete from User us where us.app_user_name = :app_user_name")
	public void deleteByAppUserName(@Param("app_user_name") String app_user_name);
}