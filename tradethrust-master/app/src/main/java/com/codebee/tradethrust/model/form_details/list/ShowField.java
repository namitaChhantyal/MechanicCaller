package com.codebee.tradethrust.model.form_details.list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShowField implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("label")
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
