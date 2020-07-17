package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.util.Log;


import androidx.multidex.BuildConfig;
import androidx.preference.PreferenceManager;


import com.glebsterd.mytodolist.R;

/**
 *
 */
public class NotificationRunnable implements Runnable {

    private static final String TAG = "NotificationRunnable";

    private volatile boolean isRunning;
    private final NotificationPusher notificationPusher;

    /**
     * Constructor
     * @param application application context
     */
    public NotificationRunnable(Application application) {

        isRunning = PreferenceManager.getDefaultSharedPreferences(application)
                .getBoolean(application.getString(R.string.pref_alarm_check_key), false);
        notificationPusher = new NotificationPusher(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        Log.d(TAG, "[Run] ---> IN");
        //isRunning = true;

        while (isRunning) {
            Log.d(TAG, "[Run] ---> [While-Loop]");
            notificationPusher.pushNotifications();
            goToSleep();
        }

        Log.d(TAG, "[Run] ---> OUT");
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
