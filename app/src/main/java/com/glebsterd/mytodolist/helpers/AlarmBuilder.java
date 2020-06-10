package com.glebsterd.mytodolist.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;
import androidx.core.content.ContextCompat;

import com.glebsterd.mytodolist.services.NotificationService;

import java.util.Calendar;

/**
 * Class for creating alarm that launch notification service
 */
public class AlarmBuilder {

    /**
     * Create new alarm if not been created before
     *
     * @param context application context
     * @param hourOfDay integer value of hour in 24-hour format
     */
    public static void createAlarm(Context context, int hourOfDay, int minuteOfDay) {

        Intent intent = new Intent(context, NotificationService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null) {

            //AlarmManagerCompat manager = (AlarmManagerCompat) context.getSystemService(Context.ALARM_SERVICE);
//            AlarmManagerCompat manager = (AlarmManagerCompat) context.getSystemService(ContextCompat.);
//            PendingIntent pendingIntent1 = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            calendar.set(Calendar.MINUTE, minuteOfDay);
//
//            // Set repeating alarm with interval of day from time that user set in settings
//            if(manager != null){
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
//            }
        }

    }// createAlarm

}// class
