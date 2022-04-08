package com.codebee.tradethrust.model.profile;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("fos_name")
	private String fosName;

	@SerializedName("id")
	private int id;

	@SerializedName("contact_person_name")
	private String contactPersonName;

	@SerializedName("user")
	private User user;

	@SerializedName("age")
	private Object age;

	@SerializedName("contact_number")
	private String contactNumber;

	@SerializedName("email")
	private String email;

	public void setFosName(String fosName){
		this.fosName = fosName;
	}

	public String getFosName(){
		return fosName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setContactPersonName(String contactPersonName){
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonName(){
		return contactPersonName;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setAge(Object age){
		this.age = age;
	}

	public Object getAge(){
		return age;
	}

	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}

	public String getContactNumber(){
		return contactNumber;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"fos_name = '" + fosName + '\'' + 
			",id = '" + id + '\'' + 
			",contact_person_name = '" + contactPersonName + '\'' + 
			",user = '" + user + '\'' + 
			",age = '" + age + '\'' + 
			",contact_number = '" + contactNumber + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}