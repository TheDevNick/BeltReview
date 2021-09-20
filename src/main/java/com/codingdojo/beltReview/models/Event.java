package com.codingdojo.beltReview.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.*;

@Entity
@Table(name="events")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min=1, message="Event name must not be empty")
	private String eventName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eventDate;
	@Size(min=1, message="city cannot be empty")
	private String city;
	private String state;
	@Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="host_id")
	private User host;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="event")
    private List<Message> eventMessages;

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
		name="users_events",
		joinColumns = @JoinColumn(name="event_id"),
		inverseJoinColumns = @JoinColumn(name="user_id")
	)
    public List<User> usersEvents;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public User getHost() {
		return host;
	}
	public void setHost(User host) {
		this.host = host;
	}
	public List<Message> getEventMessages() {
		return eventMessages;
	}
	public void setEventMessages(List<Message> eventMessages) {
		this.eventMessages = eventMessages;
	}
	public List<User> getUsersEvents() {
		return usersEvents;
	}
	public void setUsersEvents(List<User> usersEvents) {
		this.usersEvents = usersEvents;
	}
	
	public Event() {
    }
	
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }

	@PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
    
    
}
