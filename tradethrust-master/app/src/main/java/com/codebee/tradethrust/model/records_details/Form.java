package com.codebee.tradethrust.model.records_details;

import java.util.List;

import com.codebee.tradethrust.model.form_details.list.Schema;
import com.google.gson.annotations.SerializedName;

public class Form{

	@SerializedName("schema")
	private List<Schema> schema;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setSchema(List<Schema> schema){
		this.schema = schema;
	}

	public List<Schema> getSchema(){
		return schema;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Form{" + 
			"schema = '" + schema + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}