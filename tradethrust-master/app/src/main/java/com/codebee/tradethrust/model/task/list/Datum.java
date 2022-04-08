
package com.codebee.tradethrust.model.task.list;

import com.codebee.tradethrust.model.task.details.Bit;
import com.codebee.tradethrust.model.task.details.POS;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("description")
    private Object Description;
    @SerializedName("ends_date")
    private String EndsDate;
    @SerializedName("id")
    private Long Id;
    @SerializedName("start_date")
    private String StartDate;
    @SerializedName("task_type")
    private String TaskType;
    @SerializedName("title")
    private String Title;
    @SerializedName("status")
    private String Status;
    @SerializedName("bit")
    private Bit bit;
    @SerializedName("pos")
    private POS pos;

    public Object getDescription() {
        return Description;
    }

    public void setDescription(Object description) {
        Description = description;
    }

    public String getEndsDate() {
        return EndsDate;
    }

    public void setEndsDate(String endsDate) {
        EndsDate = endsDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Bit getBit() {
        return bit;
    }

    public void setBit(Bit bit) {
        this.bit = bit;
    }

    public POS getPos() {
        return pos;
    }

    public void setPos(POS pos) {
        this.pos = pos;
    }
}
