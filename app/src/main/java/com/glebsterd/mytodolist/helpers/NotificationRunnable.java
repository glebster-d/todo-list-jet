package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.util.Log;

public class NotificationRunnable implements Runnable {

    private static final String TAG = "NotificationRunnable";

    static final boolean DEBUG = true;

    private NotificationPusher notificationPusher;
    private boolean isRunning = false;

    public NotificationRunnable(Application application) {

        notificationPusher = new NotificationPusher(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        while (isRunning) {

            notificationPusher.startAlarms();
            goToSleep();
        }
    }// run

    private void goToSleep() {

        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {

            if (DEBUG) {
                Log.d(TAG, "[GoToSleep] ---> Exception: " + e.getMessage());
            }
        }
    }

    public void stop() {

        if (!Thread.interrupted())
            isRunning = false;
    }

}// class
