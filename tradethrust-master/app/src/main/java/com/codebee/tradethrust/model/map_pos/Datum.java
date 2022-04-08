
package com.codebee.tradethrust.model.map_pos;

import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("data")
    private com.codebee.tradethrust.model.map_pos.Data Data;
    @SerializedName("id")
    private Long Id;
    @SerializedName("lonlat")
    private String Lonlat;
    @SerializedName("status")
    private String Status;

    public com.codebee.tradethrust.model.map_pos.Data getData() {
        return Data;
    }

    public void setData(com.codebee.tradethrust.model.map_pos.Data data) {
        Data = data;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLonlat() {
        return Lonlat;
    }

    public void setLonlat(String lonlat) {
        Lonlat = lonlat;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
