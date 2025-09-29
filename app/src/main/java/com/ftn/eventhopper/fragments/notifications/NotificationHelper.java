package com.ftn.eventhopper.fragments.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHelper {

    public static final String CHANNEL_ID = "my_foreground_channel";

    public static void createNotificationChannel(Context context) {

            CharSequence name = "Foreground Service Channel";
            String description = "Channel for foreground service notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

    }
}
