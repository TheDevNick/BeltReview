package com.codingdojo.beltReview.controllers;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingdojo.beltReview.models.Event;
import com.codingdojo.beltReview.models.Message;
import com.codingdojo.beltReview.models.User;
import com.codingdojo.beltReview.services.EventService;
import com.codingdojo.beltReview.services.MessageService;
import com.codingdojo.beltReview.services.UserService;

import net.bytebuddy.asm.Advice.This;

@Controller
public class EventController {
//	injecting the services that will be needed
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private MessageService messageService;
	

	
	
	//	Routes for the Event details view
	
	@GetMapping("events/{id}")
	public String Show(@PathVariable("id") Long id, Model model) {
		
		Event event = eventService.findEvent(id);
		User user = userService.findById(id);
		model.addAttribute("event", event);
		model.addAttribute("user", user);
		model.addAttribute("message", messageService.allMessages());
		return "/event/viewEvent";
	}
	
	//posting message to message board
//	@PostMapping("events/{id}/comment")
//	public String Comment(@PathVariable("id") Long id, @RequestParam("comment") String comment, RedirectAttributes redirs, HttpSession session) {
//		if(comment.equals("")) {
//			redirs.addFlashAttribute("error", "Comment must not be blank");
//			return "redirect:/events/" + id ;
//		}
//		Event event = this.eventService.findEvent(id);
//		User user = this.userService.findById(id);
//		return "redirect:/events/" + id;
//	}
}
