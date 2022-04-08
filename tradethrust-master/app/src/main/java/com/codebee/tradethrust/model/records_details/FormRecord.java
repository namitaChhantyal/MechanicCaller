package com.codebee.tradethrust.model.records_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FormRecord{

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
			"FormRecord{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}