package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.util.Log;

import com.glebsterd.mytodolist.BuildConfig;

/**
 *
 */
public class NotificationRunnable implements Runnable {

    private static final String TAG = "NotificationRunnable";

    private volatile boolean isRunning;
    private final NotificationPusher notificationPusher;

    public NotificationRunnable(Application application) {

        notificationPusher = new NotificationPusher(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        isRunning = true;

        while (isRunning) {

            notificationPusher.startAlarms();
            goToSleep();
        }
    }// run

    private void goToSleep() {

        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {

            isRunning = false;

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "[GoToSleep] ---> Exception: " + e.getMessage());
            }
        }
    }// goToSleep

    public void cancel() {
        isRunning = false;
    }

}// class
