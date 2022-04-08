package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.task.details.TaskDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by csangharsha on 5/27/18.
 */

public interface TaskDetailAPI {

    @GET("/api/tasks/{taskId}")
    Call<TaskDetails> getTaskDetails(@Path("taskId") String taskId, @HeaderMap Map<String, String> headerInfo);

}
