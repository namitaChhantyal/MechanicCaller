package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.pos_filter.FilterData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FOSBitsAPI {

    @GET("/api/bits")
    Call<FilterData> getBitForFos(@HeaderMap Map<String, String> headerInfo, @Query("fos_id") String fosId);

    @GET("/api/pos_profiles")
    Call<FilterData> getPOSForFos(@HeaderMap Map<String, String> headerInfo, @Query("fos_id") String fosId);

}
