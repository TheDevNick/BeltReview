package com.codingdojo.beltReview.controllers;

import java.util.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
//	ArrayList<String> states = new ArrayList<String>(Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT",
//			"DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN",
//			"MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
//			"SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"));
	
	
	// 	Routing for the dashboard
	@RequestMapping("/events")
	public String Dashboard(@Valid @ModelAttribute("event") Event event, 
			BindingResult result, HttpSession session, Model model) {
		if(session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		User user = userService.findById((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);
		//session.setAttribute("states", states);
		List<Event> events = eventService.allEvents();
		List<Event> inStateEvent = new ArrayList<Event>();
		List<Event> notInState = new ArrayList<Event>();
		for (Event thisEvent : events) {
			if(thisEvent.getState().equals(user.getState())) {
				inStateEvent.add(thisEvent);
			} else {
				notInState.add(thisEvent);
			}
		}
		model.addAttribute("inStateEvent", inStateEvent);
		model.addAttribute("notInState", notInState);
		return "event/dashboard.html";
	}
	
	
	@GetMapping("/event/edit/{eventId}")
	public String getEditEvent(@PathVariable("eventId") Long id, 
			@ModelAttribute("event") Event event, Model model, HttpSession session){
		if(session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		User user = userService.findById((Long) session.getAttribute("userId"));
		if(eventService.findEvent(id).getHost().getId() == user.getId()) {
			model.addAttribute("event", eventService.findEvent(id));
			return "redirect:event/editEvent.html";
		} else {
			return "redirect:/events";
		}
	}
	
	@PostMapping("/event/edit/{eventId}")
	public String editEvent(@Valid @PathVariable("eventId") Long id, 
			@ModelAttribute("event") Event event, BindingResult result, 
			Model model, HttpSession session) {
		User user = userService.findById((Long) session.getAttribute("userId"));
		if(eventService.findEvent(id).getHost().getId() == user.getId()) {
			if(result.hasErrors()) {
				model.addAttribute("event", eventService.findEvent(id));
				return "/event/editEvent.html";
			} else {
				Event editEvent = eventService.findEvent(id);
				model.addAttribute("event", editEvent);
				model.addAttribute("user", user);
				event.setHost(user);
				event.setUsersEvents(event.getUsersEvents());
				eventService.editEvent(id, event);
				return "redirect:/events";
			}
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/event/addEvent")
	public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, HttpSession session) {
		if(result.hasErrors()) {
			return "event/dashboard.html";
		} else {
			eventService.create(event);
			return "redirect:/events";
		}
	}
	
	@RequestMapping("/event/delete/{eventId}")
	public String deleteEvent(@PathVariable("eventId") Long id) {
		Event event = eventService.findEvent(id);
		for(User user : event.getUsersEvents()) {
			List<Event> myEvents = user.getEventsPlanned();
			myEvents.remove(event);
			user.setEventsPlanned(myEvents);
			userService.updateUser(id, user);
		}
		eventService.deleteEvent(event);
		return "redirect:/events";
	}
	
	@RequestMapping("/event/join/{eventId")
	public String Join(@PathVariable("eventId") Long id, HttpSession session) {
		User user = userService.findById((Long) session.getAttribute("userId"));
		Event event = eventService.findEvent(id);
		List<User> attendees = event.getUsersEvents();
		attendees.add(user);
		event.setUsersEvents(attendees);
		userService.updateUser(id, user);	
		return "redirect:/events";
	}
	
	@RequestMapping("/events/cancel/{eventId}")
    public String cancelEvent(@PathVariable("id") Long id, HttpSession session) {
    	User user = userService.findById((Long) session.getAttribute("userId"));
		Event event = eventService.findEvent(id);
    	List<User> attendees = event.getUsersEvents();
        for(int i=0; i<attendees.size(); i++) {
            if(attendees.get(i).getId() == user.getId()) {
            	attendees.remove(i);
            }
        }
        event.setUsersEvents(attendees);
        userService.updateUser(id, user);
    	return "redirect:/events";
    }
	
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
