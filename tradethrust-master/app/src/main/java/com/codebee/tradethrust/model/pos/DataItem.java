package com.codebee.tradethrust.model.pos;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("pos_name")
	private String posName;

	@SerializedName("id")
	private int id;

	public void setPosName(String posName){
		this.posName = posName;
	}

	public String getPosName(){
		return posName;
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
			"DataItem{" + 
			"pos_name = '" + posName + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}