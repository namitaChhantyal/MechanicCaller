package com.codebee.tradethrust.model.task.list.group_by_pos;

import com.codebee.tradethrust.model.task.details.Data;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class POSFormDetails {

    @SerializedName("data")
    private ArrayList<com.codebee.tradethrust.model.task.details.Data> Data;

    public ArrayList<Data> getData() {
        return Data;
    }

    public void setData(ArrayList<Data> data) {
        Data = data;
    }
}
