package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.form_details.list.FormDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * Created by csangharsha on 5/26/18.
 */

public interface FormDetailsForTaskAPI {

    @GET("/api/forms/{formId}")
    Call<FormDetails> getFormDetails(@Path("formId") String id, @HeaderMap Map<String, String> headers);

}
