package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

/**
 * Created by csangharsha on 5/28/18.
 */

public class POS {

    @SerializedName("id")
    private Long Id;
    @SerializedName("name")
    private String Name;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
