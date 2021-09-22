package com.codingdojo.beltReview.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingdojo.beltReview.models.User;
import com.codingdojo.beltReview.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping("/login")
	public String getLoginForm(@ModelAttribute("user") User user) {
		return "event/loginForm";
	}
	
	@PostMapping("/login")
	public String Login(
			@Valid User user, BindingResult result,
			RedirectAttributes redirectAttributes,
			HttpSession session
			) {
		
		if (result.hasErrors()) return "loginForm";
		
		user = this.service.login(user);
		
		if ( user == null ) {
			redirectAttributes.addFlashAttribute("errors", "invalid credentials");
			return "redirect:/user/login";
		}
		
		session.setAttribute("userId", user.getId());
		
		redirectAttributes.addFlashAttribute("messages", "Welcome Back!");
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/register")
	public String getRegistrationForm(@ModelAttribute("user") User user) {
		return "event/registrationForm";
	}
	
	@PostMapping("/register")
	public String register(
			@Valid User user, BindingResult result,
			RedirectAttributes redirectAttributes
			) {
		
		if (result.hasErrors()) return "registrationForm";
		
		user = this.service.register(user);
		
		if ( user == null ) {
			redirectAttributes.addFlashAttribute("errors", "email addres is not available");
			return "redirect:/user/register";
		}
		
		
		// create session
		
		return "redirect:/login";
	}
	
	
}
