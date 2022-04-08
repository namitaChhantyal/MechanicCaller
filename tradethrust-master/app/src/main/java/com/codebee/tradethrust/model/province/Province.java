package com.codebee.tradethrust.model.province;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Province {

    @SerializedName("data")
    List<ListHolder> provinces;

    public List<ListHolder> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ListHolder> provinces) {
        this.provinces = provinces;
    }
}
