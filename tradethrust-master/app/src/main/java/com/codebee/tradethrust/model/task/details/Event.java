
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("ends_date")
    private Object EndsDate;
    @SerializedName("id")
    private Long Id;
    @SerializedName("rules")
    private com.codebee.tradethrust.model.task.details.Rules Rules;
    @SerializedName("scheduled_at")
    private Object ScheduledAt;

    public Object getEndsDate() {
        return EndsDate;
    }

    public void setEndsDate(Object endsDate) {
        EndsDate = endsDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public com.codebee.tradethrust.model.task.details.Rules getRules() {
        return Rules;
    }

    public void setRules(com.codebee.tradethrust.model.task.details.Rules rules) {
        Rules = rules;
    }

    public Object getScheduledAt() {
        return ScheduledAt;
    }

    public void setScheduledAt(Object scheduledAt) {
        ScheduledAt = scheduledAt;
    }

}
