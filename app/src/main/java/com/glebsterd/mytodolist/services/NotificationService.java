package com.glebsterd.mytodolist.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.glebsterd.mytodolist.BuildConfig;
import com.glebsterd.mytodolist.helpers.NotificationRunnable;

/**
 * Service that starts background thread that runs every minute and checks database entries.
 * If current date and date of entry are the same, service thread will trigger notification.
 */
public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    private NotificationRunnable notificationRunnable;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "[OnCreate] ---> IN ");
        }

        notificationRunnable = new NotificationRunnable(getApplication());
        Thread notificationThread = new Thread(notificationRunnable);
        notificationThread.start();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "[OnCreate] ---> OUT ");
        }

    }// onCreate

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }// onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationRunnable.cancel();
    }
}// class
