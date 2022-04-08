package com.codebee.tradethrust.model.pos_filter;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    private int id;

    @SerializedName("pos_name")
    private String posName = null;

    @SerializedName("name")
    private String bitName = null;

    public Data(int id, String bitName) {
        this.id = id;
        this.bitName = bitName;
    }

    public Data() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getBitName() {
        return bitName;
    }

    public void setBitName(String bitName) {
        this.bitName = bitName;
    }
}
