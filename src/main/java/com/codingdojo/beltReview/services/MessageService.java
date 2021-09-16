package com.codingdojo.beltReview.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.codingdojo.beltReview.models.Message;
import com.codingdojo.beltReview.respositories.MessageRepository;
@Service
public class MessageService {
	
	private final MessageRepository repository;
	
	public MessageService(MessageRepository repository) {
		this.repository = repository;
	}
	
	public List<Message> allMessages() {
		return this.repository.findAll();
	}
	
	public Message createHero(Message message) {
		return this.repository.save(message);
	}
	
	public void save(Message message) {
		this.repository.save(message);
	}
}
