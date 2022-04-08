package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.map_pos.MapPOS;
import com.codebee.tradethrust.model.task.details.TaskDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by csangharsha on 6/15/18.
 */

public interface MapPosAPI {

    @GET("api/task/{taskId}/form/{formId}/records")
    Call<MapPOS> getTaskDetails(@Path("taskId") String taskId, @Path("formId") String formId, @HeaderMap Map<String, String> headerInfo);

}
