package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.profile.Profile;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

public interface ProfileAPI {

    @GET("/api/fos_profiles/{id}/profile")
    Call<Profile> getProfileData(@Path("id") String id, @HeaderMap Map<String, String> headerInfo);

}
