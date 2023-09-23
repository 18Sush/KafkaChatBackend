package com.proj.kafkachatserver.models;


import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class User {
	
    @Id
    private Long userId;
    private boolean onlineStatus;
	public User(Long userId, boolean onlineStatus) {
		super();
		this.userId = userId;
		this.onlineStatus = onlineStatus;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	   // Getters and setters
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", onlineStatus=" + onlineStatus + "]";
	}
}
