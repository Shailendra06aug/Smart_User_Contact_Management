package com.smart.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.smart.entity.User;
import com.smart.repositry.UserRespositry;

public class UserDetailsServiceImple implements UserDetailsService {

	
//	@Autowired
//	private UserRespositry respositry;
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		
//		Optional<User> userinfo= respositry.findByEmail(username);
//
//
////		return userinfo.map(CustomUserDetail::new).orElseThrow(()-> new UsernameNotFoundException("User not found "+username));
//		return userinfo.map(CustomUserDetail::new).orElseThrow(()-> new UsernameNotFoundException("User not found please check "+username));	
//       
//	}
	
	
	
	

	@Autowired
	UserRespositry userRespositry;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
	User user=	userRespositry.getUserByUserName(username);
		
		
	
	if (user==null) {
		
		throw new UsernameNotFoundException("Could not found user !!");
	}
	
	CustomUserDetail customUserDetail= new CustomUserDetail(user);
		return customUserDetail;
	}
	
	
	

}
