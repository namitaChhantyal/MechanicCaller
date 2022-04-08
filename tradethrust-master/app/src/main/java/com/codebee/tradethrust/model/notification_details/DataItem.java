package com.codebee.tradethrust.model.notification_details;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("read")
	private boolean read;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private User user;

	@SerializedName("created_at")
	private String createdAt;

	public void setRead(boolean read){
		this.read = read;
	}

	public boolean isRead(){
		return read;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"read = '" + read + '\'' + 
			",id = '" + id + '\'' + 
			",message = '" + message + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}