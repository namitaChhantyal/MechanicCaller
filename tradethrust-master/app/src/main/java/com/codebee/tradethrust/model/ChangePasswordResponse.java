package com.codebee.tradethrust.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse{

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ChangePasswordResponse{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}