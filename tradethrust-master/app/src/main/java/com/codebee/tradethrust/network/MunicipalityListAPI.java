package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.form_details.list.Value;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MunicipalityListAPI {

    @POST("/api/municipality/search")
    Call<List<Value>> getMunicipalityList(@Body Map<String, String> map);

}
