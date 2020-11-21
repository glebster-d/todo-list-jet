package com.glebsterd.mytodolist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.services.NotificationService;


public class ServiceStarterReceiver extends BroadcastReceiver {

    private static final String TAG = "ServiceStarterReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isChecked = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_alarm_check_key), false);

        Log.d(TAG, "[OnReceive] ---> [isChecked]: " + isChecked);

        if (isChecked) {
            context.startService(new Intent(context, NotificationService.class));
        }
        else {
            context.stopService(new Intent(context, NotificationService.class));
        }
    }
}
