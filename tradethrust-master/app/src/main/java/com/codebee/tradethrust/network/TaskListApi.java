package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.task.list.TaskList;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by csangharsha on 5/25/18.
 */

public interface TaskListApi {

    @GET("/api/tasks")
    Call<TaskList> getTask(@QueryMap Map<String, String> queryParams, @Query("status") String status, @Query("device_id") String deviceId, @HeaderMap Map<String, String> headerInfo);

}
