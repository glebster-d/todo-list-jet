package com.glebsterd.mytodolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.glebsterd.mytodolist.services.NotificationService;


/**
 *
 */
public class App extends MultiDexApplication {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        setNotificationChannels();

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);

    }// onCreate

    // Set notification channel if api version greater or equals to 26
    private void setNotificationChannels() {

        // If OS version equals or greater than API 26, create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.channel_id),
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(getString(R.string.channel_description));
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setShowBadge(false);
            channel.setBypassDnd(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }// setNotificationChannels
}// class
