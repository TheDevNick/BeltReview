package com.codingdojo.beltReview.respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codingdojo.beltReview.models.Message;

public interface MessageRepository extends CrudRepository <Message, Long>{
	List<Message> findAll();
}
