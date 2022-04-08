package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.records_details.FormRecord;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskFormListAPI {

    @GET("/api/records")
    Call<FormRecord> getFormRecords(@HeaderMap Map<String, String> headerInfo, @Query("task_id") int taskId, @Query("form_id") int formId);

}
