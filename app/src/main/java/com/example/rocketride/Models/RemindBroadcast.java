package com.example.rocketride.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rocketride.R;

public class RemindBroadcast extends BroadcastReceiver {
    public static String from = "";
    public static String to = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "rem")
                .setSmallIcon(R.drawable.welcomelogo_no_background)
                .setContentTitle("your drive is about to start in soon")
                .setContentText("from: "+from+" to: "+to)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
