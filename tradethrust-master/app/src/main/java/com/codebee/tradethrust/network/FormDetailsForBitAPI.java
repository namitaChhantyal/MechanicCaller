package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.form_details.list.FormDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface FormDetailsForBitAPI {

    @GET("/api/bits/new_pos_form")
    Call<FormDetails> getFormDetails(@HeaderMap Map<String, String> headers);

}
