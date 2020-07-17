package com.glebsterd.mytodolist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.services.NotificationService;

import java.util.Objects;

/**
 * Start notification service after system boot
 */
public class ServiceStarterReceiver extends BroadcastReceiver {

    private static final String TAG = "ServiceStarterReceiver";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isChecked = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_alarm_check_key), false);

        Log.d(TAG, "[OnReceive] ---> isChecked = " + isChecked);

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            if (isChecked) {
                context.startService(new Intent(context, NotificationService.class));
            }
        }
        if (Objects.equals(intent.getAction(), "com.glebsterd.mytodolist.SETTINGS_CHANGED")) {

            if (isChecked) {
                context.startService(new Intent(context, NotificationService.class));
            }
            else {
                context.stopService(new Intent(context, NotificationService.class));
            }
        }

    }// onReceive
}// class
