
package com.codebee.tradethrust.model.form_details.create;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Record {

    @SerializedName("data")
    Map<String, Object> data;

    @SerializedName("form_id")
    private Long FormId;

    @SerializedName("task_id")
    private Long TaskId;

    @SerializedName("pos_id")
    private int PosId;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Long getFormId() {
        return FormId;
    }

    public void setFormId(Long formId) {
        FormId = formId;
    }

    public Long getTaskId() {
        return TaskId;
    }

    public void setTaskId(Long taskId) {
        TaskId = taskId;
    }

    public int getPosId() {
        return PosId;
    }

    public void setPosId(int posId) {
        PosId = posId;
    }
}
