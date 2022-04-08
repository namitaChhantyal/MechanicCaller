
package com.codebee.tradethrust.model.map_pos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MapPOS {

    @SerializedName("data")
    private List<Datum> Data;

    public List<Datum> getData() {
        return Data;
    }

    public void setData(List<Datum> data) {
        Data = data;
    }

}
