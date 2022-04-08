package com.codebee.tradethrust.model.records_details;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class DataItem{

	@SerializedName("data")
	private HashMap<String, String> data;

	@SerializedName("form")
	private Form form;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private String status;

	public void setData(HashMap<String, String> data){
		this.data = data;
	}

	public HashMap<String, String> getData(){
		return data;
	}

	public void setForm(Form form){
		this.form = form;
	}

	public Form getForm(){
		return form;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"data = '" + data + '\'' + 
			",form = '" + form + '\'' + 
			",id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}