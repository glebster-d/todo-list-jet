package com.glebsterd.mytodolist.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.glebsterd.mytodolist.helpers.NotificationRunnable;

/**
 * Service that runs every day and checks database enties. If current date and date of entry
 * are the same, service will trigger notification.
 */
public class NotificationService extends Service {

    private Thread notificationThread;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {

        notificationThread = new Thread(new NotificationRunnable(getApplication()));
        notificationThread.start();

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

}// class
