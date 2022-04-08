package com.codebee.tradethrust.model.profile;

import com.google.gson.annotations.SerializedName;

public class Profile{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Profile{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}