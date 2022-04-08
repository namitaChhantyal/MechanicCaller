package com.codebee.tradethrust.network;

import com.codebee.tradethrust.model.notification_details.Notification;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface NotificationListAPI {

    @GET("/api/notifications")
    Call<Notification> getFormRecords(@HeaderMap Map<String, String> headerInfo);


}
