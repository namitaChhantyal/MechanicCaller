package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.form_details.list.FormDetails;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by csangharsha on 6/17/18.
 */

public interface StartThisActivityAPI {

    @POST("/api/tasks/{taskId}/start")
    Call<FormDetails> updateTaskStatus(@Path("taskId") String taskId, @HeaderMap Map<String, String> headers, @Body Map<String, String> body);

}
