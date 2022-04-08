package com.codebee.tradethrust.model;

/**
 * Created by csangharsha on 5/17/18.
 */

public class Task {

    private int id;
    private String title;
    private int taskType;

    public Task(int id, String title, int taskType) {
        this.id = id;
        this.title = title;
        this.taskType = taskType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

}
