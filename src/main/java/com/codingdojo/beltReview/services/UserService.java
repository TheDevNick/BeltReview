package com.codingdojo.beltReview.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import com.codingdojo.beltReview.models.User;
import com.codingdojo.beltReview.respositories.UserRepository;

@Service
public class UserService {
	private final UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public User findUserByEmail(String email) {
    	return this.repository.findByEmail(email);
    }
    
    public User findById(Long id) {
		Optional<User> optional = this.repository.findById(id);
		if( optional.isPresent() ) {
			return optional.get();
		} else {
			return null;
		}
	}
    
    public User login(User user) {
    	User foundUser = this.findUserByEmail(user.getEmail());
    	if ( foundUser == null || !BCrypt.checkpw(user.getPassword(), foundUser.getPassword()) ) return null; 
    	return foundUser;
    }
    
    public User register(User user) {
    	if ( this.findUserByEmail(user.getEmail()) == null ) {
    		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    		user.setPassword(hashed);
    		user = this.repository.save(user);
    		return user;
    	}
    	
    	return null;
    }
}
