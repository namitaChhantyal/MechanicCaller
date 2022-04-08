package com.codebee.tradethrust.model.group_by_pos;

import com.google.gson.annotations.SerializedName;

public class TasksItem{

	@SerializedName("ends_date")
	private String endsDate;

	@SerializedName("description")
	private Object description;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	@SerializedName("start_date")
	private String startDate;

	public void setEndsDate(String endsDate){
		this.endsDate = endsDate;
	}

	public String getEndsDate(){
		return endsDate;
	}

	public void setDescription(Object description){
		this.description = description;
	}

	public Object getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"TasksItem{" + 
			"ends_date = '" + endsDate + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",status = '" + status + '\'' + 
			",start_date = '" + startDate + '\'' + 
			"}";
		}
}