package com.smart.repositry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smart.entity.Contact;
import com.smart.entity.User;

public interface ContactRepositry  extends JpaRepository<Contact, Integer>{

	
	@Query("from Contact as c where c.user.id =:userId")
	//CurrentPage
	//Contact Per Page -5
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);
	
//	@Query("from Contact as c where c.user.id =:userId")
//	public List<Contact> findByContacts(@Param("userId")int userId);
	
	//Search
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
}
