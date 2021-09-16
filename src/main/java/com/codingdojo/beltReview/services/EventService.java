package com.codingdojo.beltReview.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.codingdojo.beltReview.models.Event;
import com.codingdojo.beltReview.respositories.EventRepository;

@Service
public class EventService {
	private final EventRepository repository;
	
	public EventService(EventRepository repository) {
		this.repository = repository;
	}
	
	public List<Event> allEvents() {
		return this.repository.findAll();
	}
	
	public Event create(Event event) {
		return this.repository.save(event);
	}
	
	public Event findEvent(Long id) {
		Optional<Event> optional = this.repository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
	}
	
	public void save(Event event) {
		this.repository.save(event);
	}
	
	public Event editEvent(Long id, Event eventUpdates) {
		
		Event event = this.findEvent(id);
		
		if( event != null ) {
			event.setEventName(eventUpdates.getEventName());
			event.setEventDate(eventUpdates.getEventDate());
			event.setCity(eventUpdates.getCity());
			event.setState(eventUpdates.getState());
			this.save(event);
			return event;
		}
		
		return null;
	}
	
	public void deleteEvent(Event event) {
		this.repository.delete(event);
	}
 }
