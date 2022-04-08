
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

public class TaskDetails {

    @SerializedName("data")
    private com.codebee.tradethrust.model.task.details.Data Data;
    @SerializedName("message")
    private String Message;

    public com.codebee.tradethrust.model.task.details.Data getData() {
        return Data;
    }

    public void setData(com.codebee.tradethrust.model.task.details.Data data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
