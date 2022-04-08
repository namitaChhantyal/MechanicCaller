package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.province.Province;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface ProvinceListAPI {

    @GET("/api/provinces")
    Call<Province> getProvinceList(@HeaderMap Map<String, String> headerInfo);

    @GET("/api/zones")
    Call<Province> getZoneList(@Query("province_id") int id, @HeaderMap Map<String, String> headerInfo);

    @GET("/api/districts")
    Call<Province> getDistrictList(@Query("zone_id") int id, @HeaderMap Map<String, String> headerInfo);

    @GET("/api/municipalities")
    Call<Province> getMunicipalityList(@Query("district_id") int id, @HeaderMap Map<String, String> headerInfo);

}
