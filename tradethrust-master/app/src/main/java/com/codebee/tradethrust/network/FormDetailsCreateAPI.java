package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.form_details.create.FormDetailsCreate;
import com.codebee.tradethrust.model.form_details.list.FormDetails;
import com.codebee.tradethrust.model.login.LoginUserData;
import com.codebee.tradethrust.model.login.UserParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by csangharsha on 5/27/18.
 */
public interface FormDetailsCreateAPI {

    @Multipart
    @POST("/api/records")
    Call<FormDetails> createFormDetailsFromTask(@PartMap() Map<String, RequestBody> partMap, @Part List<MultipartBody.Part> file, @HeaderMap Map<String, String> headers);

    @POST("/api/records")
    Call<FormDetails> createFormDetailsFromTask(@Body FormDetailsCreate formDetailsCreate, @HeaderMap Map<String, String> headers);

    @Multipart
    @POST("/api/bits/create_pos")
    Call<FormDetails> createFormDetailsFromBit(@PartMap() Map<String, RequestBody> partMap, @Part List<MultipartBody.Part> file, @HeaderMap Map<String, String> headers);

    @POST("/api/bits/create_pos")
    Call<FormDetails> createFormDetailsFromBit(@Body FormDetailsCreate formDetailsCreate, @HeaderMap Map<String, String> headers);

}
