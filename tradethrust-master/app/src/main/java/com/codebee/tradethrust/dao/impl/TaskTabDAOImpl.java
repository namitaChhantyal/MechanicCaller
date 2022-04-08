package com.codebee.tradethrust.dao.impl;

import com.codebee.tradethrust.dao.TaskTabDAO;
import com.codebee.tradethrust.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csangharsha on 5/18/18.
 */

public class TaskTabDAOImpl implements TaskTabDAO {

    private static List<Task> taskList = new ArrayList<>();

    public TaskTabDAOImpl() {
        setTask();
    }

    private void setTask() {
        Task task = new Task(1, "Collect POS from Lazimpat Area", 1);
        taskList.add(task);

        task = new Task(1, "Merchandise in Route Lazimpat Area", 1);
        taskList.add(task);

        task = new Task(1, "Merchandise Ramesh and Sons Trade", 1);
        taskList.add(task);

        task = new Task(1, "Merchandise in Route Lazimpat Area and do some xyz for 5 POS", 1);
        taskList.add(task);

        task = new Task(1, "Perform weekly visit to Kupondole route and check the merchandising is intact or not", 1);
        taskList.add(task);

        task = new Task(1, "Survey in the Kalanki Route about compititors merchandising", 1);
        taskList.add(task);

        task = new Task(1, "Change Hoarding boards in Route Lazimpat to Maharajgunj", 2);
        taskList.add(task);

        task = new Task(1, "Change the hoarding board of the Ramesh Traders", 2);
        taskList.add(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskList;
    }

    @Override
    public List<Task> getAllTasksOfType(int taskType) {
        List<Task> taskListTemp = new ArrayList<>();

        for(Task task:taskList) {
            if(task.getTaskType()==taskType) {
                taskListTemp.add(task);
            }
        }

        return taskListTemp;
    }
}
