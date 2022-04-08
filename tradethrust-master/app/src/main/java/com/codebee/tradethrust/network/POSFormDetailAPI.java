package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.task.list.group_by_pos.POSFormDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface POSFormDetailAPI {

    @GET("api/tasks")
    Call<POSFormDetails> getPOSFormDetails(@Query("bit_pos_id") String taskId, @HeaderMap Map<String, String> headerInfo);


}
