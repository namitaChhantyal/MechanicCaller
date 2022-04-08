
package com.codebee.tradethrust.model.form_details.list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dagger.Component;

public class Schema implements Serializable {

    @SerializedName("label")
    private String Label;
    @SerializedName("name")
    private String Name;
    @SerializedName("type")
    private String Type;
    @SerializedName("required")
    private boolean Required;
    @SerializedName("min")
    private Integer Min;
    @SerializedName("max")
    private Integer Max;
    @SerializedName("subtype")
    private String Subtype;
    @SerializedName("values")
    private ArrayList<Value> Values;
    @SerializedName("dependable")
    private Boolean Dependable = false;
    @SerializedName("cloneable")
    private ArrayList<Schema> Cloneable = new ArrayList<>();

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isRequired() {
        return Required;
    }

    public void setRequired(boolean required) {
        Required = required;
    }

    public ArrayList<Value> getValues() {
        return Values;
    }

    public void setValues(ArrayList<Value> values) {
        Values = values;
    }

    public Integer getMin() {
        return Min;
    }

    public void setMin(Integer min) {
        Min = min;
    }

    public Integer getMax() {
        return Max;
    }

    public void setMax(Integer max) {
        Max = max;
    }

    public String getSubtype() {
        return Subtype;
    }

    public void setSubtype(String subtype) {
        Subtype = subtype;
    }

    public Boolean getDependable() {
        return Dependable;
    }

    public void setDependable(Boolean dependable) {
        Dependable = dependable;
    }

    public ArrayList<Schema> getCloneable() {
        return Cloneable;
    }

    public void setCloneable(ArrayList<Schema> cloneable) {
        Cloneable = cloneable;
    }
}
