package com.glebsterd.mytodolist.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.glebsterd.mytodolist.helpers.NotificationRunnable;


public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    private NotificationRunnable notificationRunnable;


    @Override
    public void onCreate() {

        Log.d(TAG, "[OnCreate] ---> IN");

        notificationRunnable = new NotificationRunnable(getApplication());
        Thread notificationThread = new Thread(notificationRunnable);
        notificationThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "[OnDestroy] ---> IN");
        super.onDestroy();
        notificationRunnable.cancel();
    }
}
