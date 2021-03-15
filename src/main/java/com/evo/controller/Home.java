package com.evo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.bridge.Message;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.bytebuddy.utility.RandomString;

@Controller
public class Home {

	private Object userRepository;


	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title" , "");
		return "register";
	}
	

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)

	public String registerUser(@Valid @ModelAttribute("user") User user, 
			
			@RequestParam("profileImage") MultipartFile file 
			,BindingResult result1, @RequestParam(value = "agreement", defaultValue = "false")boolean agreement, Model model,
			
			HttpSession session) {
   	 model.addAttribute("title" , "Register Here");
   	try {
   		 if(!agreement) {
       		 System.out.println("You have not ageed the terms and conditions");
       		 throw new Exception("You have not ageed the terms and conditions");
       	 }
       	 
   		 if(result1.hasErrors()) {
   			 System.out.println("ERROR" + result1.toString());
   			 model.addAttribute("user",user);
   			 return "signup";
   		 }
   			
       
       	 
       	 
       	 user.setPassword(passwordEncoder.encode(user.getPassword()));
       	 
       	 System.out.println("Agreement "+agreement);
   	System.out.println("USER "+user);
   	
   	
   	//verification-code
   	String randomCode=RandomString.make(64);
   	user.setVerificationCode(randomCode);
   	
   	User result = this.userRepository.save(user);
   	

   	
   	model.addAttribute("user", new User());
   	
   	session.setAttribute("message", new Message("Successfully registered please login !!", "alert-success", null, null, null, null));
		
   	return "signup";
   		
		} catch (Exception e) {
			
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong !!"+e.getMessage(), "alert-danger", null, null, e, null));
			return "signup";
				
		}
	}
	
	
	@RequestMapping("/signin")
	public String login(Model model)
	{
		model.addAttribute("title" , "");
		return "login";
	}
	
}
