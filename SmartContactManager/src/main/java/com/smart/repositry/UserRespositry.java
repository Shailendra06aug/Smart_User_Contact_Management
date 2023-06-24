package com.smart.repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.User;


@Repository
public interface UserRespositry  extends JpaRepository<User, Integer>{

	
	 @Query("select u from User u where u.email = :email")
	 public User getUserByUserName(@Param("email") String email);
	
//	 public Optional<User> findByEmail(String email);
	

}
