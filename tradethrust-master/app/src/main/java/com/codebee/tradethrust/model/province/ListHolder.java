package com.codebee.tradethrust.model.province;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListHolder{

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("label")
    private String label;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    @Override
    public String toString(){
        return
                "ListHolder{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",label = '" + label + '\'' +
                        "}";
    }
}
