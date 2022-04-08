package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.pos.POS;
import com.codebee.tradethrust.model.province.Province;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface POSListAPI {

    @GET(" /api/pos_profiles")
    Call<POS> getPOSList(@HeaderMap Map<String, String> headerInfo);

}
