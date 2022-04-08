package com.codebee.tradethrust.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.view.acitivity.CompanyDetailsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String channelId = "Default";

        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.notification_body, remoteMessage.getData().get("body"));

        Intent ii = new Intent(this.getApplicationContext(), CompanyDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);


        Notification customNotification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, customNotification);

    }
}
