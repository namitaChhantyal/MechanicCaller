
package com.codebee.tradethrust.model.form_details.list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Value implements Serializable {

    @SerializedName("label")
    private String Label;
    @SerializedName("selected")
    private Boolean Selected;
    @SerializedName("value")
    private String Value;
    @SerializedName("showFields")
    private ArrayList<ShowField> ShowFields;

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public ArrayList<ShowField> getShowFields() {
        return ShowFields;
    }

    public void setShowFields(ArrayList<ShowField> showFields) {
        ShowFields = showFields;
    }
}
