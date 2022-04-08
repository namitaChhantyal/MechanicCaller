package com.codebee.tradethrust.model.notification_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Notification{

	@SerializedName("data")
	private List<DataItem> data;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"Notification{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}