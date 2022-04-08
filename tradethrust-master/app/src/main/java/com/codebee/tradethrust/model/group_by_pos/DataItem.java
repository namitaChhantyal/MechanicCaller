package com.codebee.tradethrust.model.group_by_pos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("pos_name")
	private String name;

	@SerializedName("id")
	private Long id;

	@SerializedName("status")
	private String status;

	@SerializedName("tasks")
	private List<TasksItem> tasks;

	@SerializedName("tasks_count")
    private Integer taskCount;

    @SerializedName("created_at")
	private String createdBy;

    public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}

	public void setTasks(List<TasksItem> tasks){
		this.tasks = tasks;
	}

	public List<TasksItem> getTasks(){
		return tasks;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public String toString(){
        return
                "DataItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",tasks = '" + tasks + '\'' +
                        "}";
    }
}