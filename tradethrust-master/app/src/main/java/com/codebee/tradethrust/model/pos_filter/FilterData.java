package com.codebee.tradethrust.model.pos_filter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterData {

    @SerializedName("data")
    private List<Data> data;

    public FilterData() {
    }

    public FilterData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
