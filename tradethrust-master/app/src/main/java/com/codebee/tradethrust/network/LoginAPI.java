package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.login.LoginUserData;
import com.codebee.tradethrust.model.login.UserParam;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by csangharsha on 5/23/18.
 */

public interface LoginAPI {

    @POST("/api/auth/sign_in")
    Call<LoginUserData> auth(@Body UserParam userParam, @HeaderMap Map<String, String> headerInfo);

}
