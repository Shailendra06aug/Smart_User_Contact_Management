package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;
import com.smart.repositry.ContactRepositry;
import com.smart.repositry.UserRespositry;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRespositry respositry;
	
	@Autowired
	private ContactRepositry contactRepositry;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;



	@ModelAttribute
	public void addCommonData(Model model, Principal principal, User user) {

		String username = principal.getName();
		System.out.println("Username " + username);

		// Get user by username(email)

		user = respositry.getUserByUserName(username);

		System.out.print("User " + user);

		model.addAttribute("user", user);
	
		
	}
	
	

	//Dashboard home
	@RequestMapping("/index")
	public String dashBoard( User user, Model model, Principal principal) {

		model.addAttribute("title", "Dashboard");
		return "normal/user_dashBoard";
	}

	
	// Open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	
//	 Processing Add contact form
	@PostMapping("/process-contact")
	private String ProcessContact(@ModelAttribute Contact contact,Principal principal, @RequestParam("profileImage") MultipartFile file, HttpSession session) {
		
		try {
			
			
			String name = principal.getName();
			
			System.out.println("Name = "+name);
			
			User user= respositry.getUserByUserName(name);
			
			//Processing and uploading image
			if (file.isEmpty()) {
				
				System.out.println("File empty ");
				contact.setImage("contact.jpg");
			}
			
			else {
			
				//File upload
				contact.setImage(file.getOriginalFilename());
				
				  File saveFile = new ClassPathResource("static/img").getFile();
				  
				  Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator+file.getOriginalFilename());
				  
				  Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				
				  System.out.println("Image sucessfully uploaded ");
				
			}
			
			user.getContact().add(contact);
		   
			contact.setUser(user);
			
			respositry.save(user);
			
			System.out.println("Data = "+contact);
			System.out.println("Sucessflly Added to database ");
			
			//Sucess Message
			session.setAttribute("message", new Message("Your contact is added !! Add more ", "success"));
			
		} 
		 catch (Exception e) {
			 
			System.out.println("ERROR "+e.getMessage());
			
			e.printStackTrace();	
			
			//Error Message
			session.setAttribute("message", new Message("Something went wrong !! Try again ", "danger"));
			
		 }
		
		
		
		return "normal/add_contact_form";
	}
	
	//Show Contact Handler
	//Per page = 5 contact
	//CurrentPage = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContact( @PathVariable("page") Integer page, Model model, Principal principal) {
		
		model.addAttribute("title", "Show User Contacts");
		
		//Contact ki list bhejni hai
		
		  String username= principal.getName();
	     User user=   respositry.getUserByUserName(username);
		 
//	     PageRequest of = PageRequest.of(page,5);
	     
	   //CurrentPage
	 	//Contact Per Page -5
	     Pageable pageable = PageRequest.of(page,5);
	     
		Page<Contact> contacts = contactRepositry.findContactsByUser(user.getId(), pageable );
//	     List<Contact> contacts = contactRepositry.findByContacts(user.getId()); // Another method for testing
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return"normal/show_contacts";
	}
	
	
	//Show Singe Contact Details
//	@RequestMapping("/{cId}/contact/")
	@GetMapping("/{cId}/contact/")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		System.out.println("Cid "+cId);
		
		
		Optional<Contact> optionalContact = this.contactRepositry.findById(cId);
		
		Contact contact= optionalContact.get();
		
        String userName = principal.getName();
		
		User user = this.respositry.getUserByUserName(userName);
		
		    if (user.getId() == contact.getUser().getId()) {
				
		    	model.addAttribute("contact", contact);
		    	model.addAttribute("title", contact.getName());
		    	System.out.println("ContactData  = "+contact);
			}

		
		return"normal/contact_details";
	}
	
	//DeleteContact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Principal principal, Model model, HttpSession session) {
		
//	    Optional<Contact> contactOptional = contactRepositry.findById(cId);
//	  
//	    Contact contact = contactOptional.get();
//	    contact.setUser(null);
//	    
//	    String userName = principal.getName();
//	    
//	    User user = respositry.getUserByUserName(userName);
//		contactRepositry.delete(contact);
	   
		Contact contact= contactRepositry.findById(cId).get();
	
		 User user = respositry.getUserByUserName(principal.getName());
		 user.getContact().remove(contact);
	       respositry.save(user);
    	
    	session.setAttribute("message", new Message("Contact delete succesfully....", "success"));
	    
    	

	    
		return"redirect:/user/show-contacts/0";
	}
	
	
	
	//Open Update form Handler
	@PostMapping("/update_Contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cId, Model model){
		
		model.addAttribute("title", "Update Contact");
		
		Contact contact = contactRepositry.findById(cId).get();
		
		model.addAttribute("contact", contact);
		
		return"normal/update_form";
	}
	
	
	
	
	//Update Contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, Principal principal,HttpSession session) {
		
		try {
			
			   Contact oldContact = contactRepositry.findById(contact.getcId()).get();
			
			if (!file.isEmpty()) {
				//Agar file present h to rewrite karni h
				
				//Delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContact.getImage());
				file1.delete();
				
				
				
				//Update new photo
				File savefile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
				
				
				
			}
			else {
				contact.setImage(oldContact.getImage());
			}
			
			
			String Username = principal.getName();

		    User user = respositry.getUserByUserName(Username);
		    
		    contact.setUser(user);
		    
		    contactRepositry.save(contact);
			
		    session.setAttribute("message", new Message("Your contact is updated...", "success"));
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println("CONTACT  Name = "+contact.getName());
		
		return"redirect:/user/"+contact.getcId()+"/contact/";
	}
	
	
	//Profile Details
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title", "Profile page");
		
		return"normal/profile_details";
	}
	
	
	//Setting change
	@GetMapping("/setting")
	public String setting() {
		
		return"normal/setting";
	}
	
	
	//Password Change
	 @PostMapping("/change-password")
	  public String passwardMatch(@RequestParam("oldPassword") String oldPassword,
			   @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
		
		 
		User currentUser=  respositry.getUserByUserName(principal.getName());
		System.out.println("old password "+currentUser.getPassword());
		 System.out.println("new passward "+newPassword);
		 System.out.println("Current user "+currentUser);
		 
		
		if (bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			
			//Change the passwaord
			
		 currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
		 respositry.save(currentUser);
		 
		 session.setAttribute("message", new Message("Password successfully updated ", "success"));
		}
		
		else {
			//error...
			
			session.setAttribute("message", new Message("Wrong password try again !! ", "danger"));
//			return"normal/setting";
			return"redirect:/user/setting";
		}
		
		
		
		
		  return"redirect:/user/index";
	  }
	
	
	
	
	
	
      
	
	
	

	
	
	

//	//Dashboard home
//	@RequestMapping("/index")
//	public String dashBoard( User user, Model model, Principal principal) {
//
//		String username = principal.getName();
//		System.out.println("Username " + username);
//
//		// Get user by username(email)
//
//		user = respositry.getUserByUserName(username);
//
//		System.out.print("User " + user);
//
//		model.addAttribute("user", user);
//
//		return "normal/user_dashBoard";
//	}
	
	
}
