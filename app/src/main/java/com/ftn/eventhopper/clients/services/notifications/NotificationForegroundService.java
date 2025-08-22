package com.ftn.eventhopper.clients.services.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.notifications.NotificationHelper;

import android.app.Service;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Context;


public class NotificationForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        // Kanal za obaveznu (foreground) notifikaciju
        NotificationChannel serviceChannel = new NotificationChannel(
                NotificationHelper.CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
        );

        // Kanal za korisničke notifikacije
        NotificationChannel userChannel = new NotificationChannel(
                "USER_NOTIFICATIONS",
                "User Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        userChannel.enableVibration(true);
        userChannel.enableLights(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
            manager.createNotificationChannel(userChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Obavezna foreground notifikacija (tiha)
        Notification notification = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
                .setContentTitle("EventHopper")
                .setContentText("Foreground service is running...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);

        // Probna korisnička notifikacija
        sendUserNotification("EventHopper", "Foreground service je pokrenut!");

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendUserNotification(String title, String message) {
        Notification notification = new NotificationCompat.Builder(this, "USER_NOTIFICATIONS")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), notification);
    }

}
