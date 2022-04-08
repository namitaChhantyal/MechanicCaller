
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("bit")
    private com.codebee.tradethrust.model.task.details.Bit Bit;
    @SerializedName("description")
    private Object Description;
    @SerializedName("ends_date")
    private String EndsDate;
    @SerializedName("events")
    private ArrayList<Event> Events;
    @SerializedName("form")
    private com.codebee.tradethrust.model.task.details.Form Form;
    @SerializedName("fos")
    private com.codebee.tradethrust.model.task.details.Fos Fos;
    @SerializedName("id")
    private Long Id;
    @SerializedName("pos")
    private ArrayList<POS> Pos;
    @SerializedName("start_date")
    private String StartDate;
    @SerializedName("task_type")
    private String TaskType;
    @SerializedName("title")
    private String Title;
    @SerializedName("status")
    private String status;

    public com.codebee.tradethrust.model.task.details.Bit getBit() {
        return Bit;
    }

    public void setBit(com.codebee.tradethrust.model.task.details.Bit bit) {
        Bit = bit;
    }

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

    public ArrayList<Event> getEvents() {
        return Events;
    }

    public void setEvents(ArrayList<Event> events) {
        Events = events;
    }

    public com.codebee.tradethrust.model.task.details.Form getForm() {
        return Form;
    }

    public void setForm(com.codebee.tradethrust.model.task.details.Form form) {
        Form = form;
    }

    public com.codebee.tradethrust.model.task.details.Fos getFos() {
        return Fos;
    }

    public void setFos(com.codebee.tradethrust.model.task.details.Fos fos) {
        Fos = fos;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public ArrayList<POS> getPos() {
        return Pos;
    }

    public void setPos(ArrayList<POS> pos) {
        Pos = pos;
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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
