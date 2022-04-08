
package com.codebee.tradethrust.model.task.list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskList {

    @SerializedName("data")
    private List<Datum> Data;

    public List<Datum> getData() {
        return Data;
    }

    public void setData(List<Datum> data) {
        Data = data;
    }

}
