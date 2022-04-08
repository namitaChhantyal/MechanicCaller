
package com.codebee.tradethrust.model.form_details.list;

import com.google.gson.annotations.SerializedName;

public class FormDetails {

    @SerializedName("data")
    private Datum Data;

    public Datum getData() {
        return Data;
    }

    public void setData(Datum data) {
        Data = data;
    }

}
