package com.glebsterd.mytodolist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.glebsterd.mytodolist.BuildConfig;
import com.glebsterd.mytodolist.services.NotificationService;

import java.util.Objects;

/**
 * Start notification service after system boot
 */
public class NotificationServiceReceiver extends BroadcastReceiver {


    private static final String TAG = "ServiceReceiver";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "[OnReceive] ---> IN ");
        }
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            context.startService(new Intent(context, NotificationService.class));
        }
    }// onReceive
}// class
