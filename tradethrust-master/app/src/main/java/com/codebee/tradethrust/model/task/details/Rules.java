
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rules implements Serializable {

    @SerializedName("day")
    private Object Day;
    @SerializedName("frequencyType")
    private String FrequencyType;
    @SerializedName("repeatDate")
    private Object RepeatDate;
    @SerializedName("repeatDays")
    private Object RepeatDays;

    public Object getDay() {
        return Day;
    }

    public void setDay(Object day) {
        Day = day;
    }

    public String getFrequencyType() {
        return FrequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        FrequencyType = frequencyType;
    }

    public Object getRepeatDate() {
        return RepeatDate;
    }

    public void setRepeatDate(Object repeatDate) {
        RepeatDate = repeatDate;
    }

    public Object getRepeatDays() {
        return RepeatDays;
    }

    public void setRepeatDays(Object repeatDays) {
        RepeatDays = repeatDays;
    }

}
