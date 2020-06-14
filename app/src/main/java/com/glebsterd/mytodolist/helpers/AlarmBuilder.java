package com.glebsterd.mytodolist.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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

        // TODO: 11/06/2020 Add option for AM-PM clock 
        Intent intent = new Intent(context, NotificationService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfDay);

        // Set repeating alarm with interval of day from time that user set in settings
        if(manager != null){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            else {
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

    }// createAlarm

}// class
