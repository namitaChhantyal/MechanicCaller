package com.codebee.tradethrust.model.group_by_pos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class POSGroupList {

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
			"POSGroupList{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}