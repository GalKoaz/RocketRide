package com.example.rocketride;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessageActivity extends FirebaseMessagingService {

//    // Create the notification channel.
//    String channelId = getString(R.string.default_notification_channel_id);
//    String channelName = getString(R.string.default_notification_channel_name);
//    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//    channel.setDescription(getString(R.string.default_notification_channel_description));
//    NotificationManager notificationManager = getSystemService(NotificationManager.class);
//    notificationManager.createNotificationChannel(channel);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Handle data payload of the message.
            handleDataMessage(remoteMessage);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // Handle notification message.
           // handleNotificationMessage(remoteMessage);
        }
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String message = data.get("message");
        String title = data.get("title");
//
//        // Create the notification.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Show the notification.
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//
//    private void handleNotificationMessage(RemoteMessage remoteMessage) {
//        String message = Objects.requireNonNull(remoteMessage.getNotification()).getBody();
//        String title = remoteMessage.getNotification().getTitle();
//
//        // Create the notification.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Show the notification.
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

// to the manifest
//<service android:name=".MyFirebaseMessagingService">
//    <intent-filter>
//        <action android:name="com.google.firebase.MESSAGING_EVENT" />
//    </intent-filter>
//</service>
}