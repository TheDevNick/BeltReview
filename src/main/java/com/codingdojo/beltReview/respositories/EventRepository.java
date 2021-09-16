package com.codingdojo.beltReview.respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codingdojo.beltReview.models.Event;

public interface EventRepository extends CrudRepository <Event, Long>{
	List<Event> findAll();
}
