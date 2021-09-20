package com.codingdojo.beltReview.respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codingdojo.beltReview.models.User;

public interface UserRepository extends CrudRepository <User, Long> {
	List<User> findAll();
	
	User findByEmail(String email);
}
