
package com.codebee.tradethrust.model.form_details.list;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    private Long Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("schema")
    private List<com.codebee.tradethrust.model.form_details.list.Schema> Schema;

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

    public List<com.codebee.tradethrust.model.form_details.list.Schema> getSchema() {
        return Schema;
    }

    public void setSchema(List<com.codebee.tradethrust.model.form_details.list.Schema> schema) {
        Schema = schema;
    }

}
