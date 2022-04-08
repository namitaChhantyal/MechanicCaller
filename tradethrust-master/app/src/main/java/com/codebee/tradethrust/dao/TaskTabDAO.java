package com.codebee.tradethrust.dao;

import com.codebee.tradethrust.model.Task;

import java.util.List;

/**
 * Created by csangharsha on 5/18/18.
 */

public interface TaskTabDAO {

    List<Task> getAllTasks();

    List<Task> getAllTasksOfType(int taskType);

}
