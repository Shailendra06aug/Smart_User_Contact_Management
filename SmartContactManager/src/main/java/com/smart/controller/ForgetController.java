package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entity.User;
import com.smart.repositry.UserRespositry;
import com.smart.service.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {
	
	Random random= new Random(1000);
	
	@Autowired
	private SessionHelper service;
	
	@Autowired
	private UserRespositry respositry;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	
	//email id form open handler
	@GetMapping("/forget")
	public String openEmailForm() {
		
		
		return"forget_email_form";
	}
	
	

	
	//Sending OTP
	@PostMapping("/send-OTP")
	public String sendingOTP(@RequestParam("email") String email, HttpSession session) {
		
		System.out.println("Email "+email);
		
		//Generate 4 digit random number
		
		int otp = random.nextInt(999999);
		System.out.println(otp);
		
		// Write a code for send otp email...
		
		String to=email;
		String subject="OTP From SCM";
//		String message="OTP = "+otp;
		String message=""
				+"<div style ='border:1px solid green; padding:20px'>"
				+"<h1>"
				+"OTP is "
				+ "<b>"+otp
				+ "</b>"
				+ "</h1>"
				+"</div>";
		
		
	boolean flag=	service.Send_email(to, subject, message);
	
	if (flag) {
		
//		session.setAttribute("message","otp");
		
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		
		return"verified-OTP";
		
	}
		else {
			session.setAttribute("message","Check your email  !!");
			
			return"forget_email_form";
			
		}

	}
	
	
	//verify otp
	@PostMapping("/verify-otp")
	private String verifyotp(@RequestParam("otp") int otp, HttpSession session ) {
		
	
		
		int myOtp = (int) session.getAttribute("myotp");
		
		System.out.println("User otp = "+otp);
		System.out.println("Myotp = "+myOtp);
		
		String email= (String) session.getAttribute("email");
		
		
		if (myOtp==otp) {
		//password change form
			
			User user = respositry.getUserByUserName(email);
			if (user==null) {
				
				//Send error  message
				session.setAttribute("message", "User does not exit with this email!!");
				return"forget_email_form";
			}
			
			else {
				//send change password form
				return"password-change-form";
			}
			
		}
		
		else {
			session.setAttribute("message", "You have entered wrong OTP !!");
			return"verified-OTP";
		}
	}
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		
		String email= (String) session.getAttribute("email");
		User userB = this.respositry.getUserByUserName(email);
		userB.setPassword(bCrypt.encode(newpassword));
		this.respositry.save(userB); 
//		return"redirect:/signin";
		return"redirect:/signin?change=password changed successfully..";
		
	}
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
