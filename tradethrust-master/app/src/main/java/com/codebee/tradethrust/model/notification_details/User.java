package com.codebee.tradethrust.model.notification_details;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("fos_id")
	private int fosId;

	@SerializedName("name")
	private String name;

	@SerializedName("sign_in_count")
	private int signInCount;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

	@SerializedName("contact_number")
	private String contactNumber;

	public void setFosId(int fosId){
		this.fosId = fosId;
	}

	public int getFosId(){
		return fosId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSignInCount(int signInCount){
		this.signInCount = signInCount;
	}

	public int getSignInCount(){
		return signInCount;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}

	public String getContactNumber(){
		return contactNumber;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"fos_id = '" + fosId + '\'' + 
			",name = '" + name + '\'' + 
			",sign_in_count = '" + signInCount + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",contact_number = '" + contactNumber + '\'' + 
			"}";
		}
}