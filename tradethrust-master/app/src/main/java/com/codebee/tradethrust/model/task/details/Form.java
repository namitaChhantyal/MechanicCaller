
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Form implements Serializable {

    @SerializedName("id")
    private Long Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("schema")
    private List<com.codebee.tradethrust.model.task.details.Schema> Schema;
    @SerializedName("form_type")
    private String formType;

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

    public List<com.codebee.tradethrust.model.task.details.Schema> getSchema() {
        return Schema;
    }

    public void setSchema(List<com.codebee.tradethrust.model.task.details.Schema> schema) {
        Schema = schema;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
