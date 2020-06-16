package com.glebsterd.mytodolist.receivers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.helpers.AlarmBuilder;
import com.glebsterd.mytodolist.services.NotificationService;

import java.util.Objects;

/**
 * Reset alarm after system boot
 */
public class AlarmAfterBootReceiver extends BroadcastReceiver {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//            String rawTime = pref.getString(context.getResources().getString(R.string.pref_alarm_time_key), "");
//            String[] time = rawTime.split(":");
//            int hour = Integer.parseInt(time[0]);
//            int minute = Integer.parseInt(time[1]);
//            AlarmBuilder.createAlarm(context, hour, minute);

            context.startService(new Intent(context, NotificationService.class));

        }
    }// onReceive
}
