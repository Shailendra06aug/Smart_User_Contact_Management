package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.repositry.ContactRepositry;
import com.smart.repositry.UserRespositry;


@RestController
public class SearchController {

	@Autowired
	private ContactRepositry contactRepositry;
	@Autowired
	private UserRespositry respositry;
	
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> serach(@PathVariable("query") String query, Principal principal){
	
		      
	System.out.println(query);	
	
	User currentUser = respositry.getUserByUserName(principal.getName());
	
	List<Contact> contacts = contactRepositry.findByNameContainingAndUser(query, currentUser);
	
	return ResponseEntity.ok(contacts);
		
	}
	
	
	
	
}
