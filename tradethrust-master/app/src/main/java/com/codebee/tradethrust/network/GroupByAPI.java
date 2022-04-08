package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.group_by_pos.POSGroupList;
import com.codebee.tradethrust.model.task.list.group_by_bit.BitFromDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GroupByAPI {

    @GET("/api/bits")
    Call<BitFromDetails> getGroupByBITList(@Query("fos_id") String fosId, @HeaderMap Map<String, String> headers);

    @GET("/api/pos_profiles")
    Call<POSGroupList> getGroupByPOSList(@QueryMap Map<String, String> queryParams, @HeaderMap Map<String, String> headers);

}
