package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entity.User;
import com.smart.helper.Message;
import com.smart.repositry.UserRespositry;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRespositry userRespositry;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/newLogin")
	public String newLogin(Model model) {
		
		model.addAttribute("title", "Login page");
		return"newLogin";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return"signup";
	}
	
	//handler for custome login user
	@RequestMapping(value =  "/do_register", method=RequestMethod.POST)
	public String do_register(@Valid @ModelAttribute("user") User user,BindingResult results,
			@RequestParam(value = "aggrement",defaultValue = "false") boolean aggrement,Model model,HttpSession session ) {
		
		
		try {
			
			if(aggrement==false) {
				System.out.println("You have not aggreed term and condition");
				throw new Exception("You have not aggreed term and condition");
			}
			
			
			if (results.hasErrors()) {
				
			    System.out.println("Error "+results);
				model.addAttribute("user", user);
				return"signup";
			}
			
			System.out.println("Aggremenet "+aggrement);		
			System.out.println("User"+user);
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default");
             
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			
		    User result=	userRespositry.save(user);
//		    model.addAttribute("user", result);
		    model.addAttribute("user", new User());
		    
		    session.setAttribute("message", new Message("Successfully Registerd !!", "alert-success"));
			return"signup";
			
		} catch (Exception e) {
		
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(), "alert-danger"));
			return"signup";
		}
		
		
		
	}
	
	
	//handel for custom login
	@GetMapping("/signin")
     public String customLogin(Model model) {
		
		model.addAttribute("title", "Login page");
    	 return "login";
	}
	
	
	
	
	
}
