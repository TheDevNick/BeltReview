package com.codingdojo.beltReview.models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(name = "messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 1, message = "message connot be empty")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	   @ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="userMessage_id")
		private User host;
	   
	   @ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="eventMessage_id")
		private Event event;
	

	@Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
		public Message() {
    }

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

}
