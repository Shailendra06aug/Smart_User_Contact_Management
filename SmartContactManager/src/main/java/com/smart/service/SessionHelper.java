package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionHelper {

	public void removeMessage() {
		
		try {
			
			HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	
	
	public boolean Send_email(String to, String subject,  String message) {
		
        boolean f=false;

       String from="shailendra06aug@gmail.com";


     	// variable for gmail
		String host="smtp.gmail.com";
		
		//get system properties
		Properties properties = System.getProperties();
		System.out.println(properties);
		
		//setting important information to properties object
		
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		//Step 1:to get session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
//	
				return new PasswordAuthentication("shailendra06aug@gmail.com", "zvxtrjinilqobztn");
			}
			
			
		});
		
		session.setDebug(true);
		
		//Step 2: Compose the message(text,multimedia)
//		  MimeMessage mimeMessage = new MimeMessage(session);
		
		  MimeMessage mimeMessage = new MimeMessage(session);
		  
		   try {
			 //From address 
			mimeMessage.setFrom(from);
			
			//adding recipent to message
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//adding subject
			mimeMessage.setSubject(subject);
			
			// adding text to message
			//mimeMessage.setText(message);
			mimeMessage.setContent(message,"text/html");
			
			//Send
			//Step :3 sed message using transport class
			Transport.send(mimeMessage);
			
			
			System.out.println("Sent successfully  ......!!");
			
			f=true;
			
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		  

        return f;

}
	
	
}
