
package com.codebee.tradethrust.model.login;

import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("id")
    private Long Id;
    @SerializedName("label")
    private String Label;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

}
