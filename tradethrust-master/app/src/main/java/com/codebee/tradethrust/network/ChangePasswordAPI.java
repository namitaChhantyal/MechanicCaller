package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.ChangePasswordResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ChangePasswordAPI {

    @PATCH("/api/users/{id}/update_password")
    Call<ChangePasswordResponse> changePassword(@Path("id") String id, @Body Map<String, String> body, @HeaderMap Map<String, String> headers);


}
