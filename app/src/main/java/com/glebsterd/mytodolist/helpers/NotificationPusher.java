package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public final class NotificationPusher {

    private static final int NOTIFICATION = 30;

    private NotificationManagerCompat notificationManager;
    private Application application;
    private Calendar calendar;

    public NotificationPusher(Application application) {
        this.application = application;
    }

    public final void startAlarms() {

        notificationManager = NotificationManagerCompat.from(application);
        EventRepository eventRepository = new EventRepository(application);
        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        String dateNow = java.text.DateFormat.getDateInstance().format(calendar.getTime());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(application);
        String preferenceTime = pref.getString(application.getResources().getString(R.string.pref_alarm_time_key), "0");

        List<Event> eventList = eventRepository.getAllEventsSortedByDate(dateNow).getValue();

        if (eventList != null) {

            for (Event event: eventList) {

                int notificationHour , notificationMinute;
                String [] raw = event.getTime().split("[: ]+");

                if(DateFormat.is24HourFormat(application)) {

                    notificationHour = Integer.parseInt(raw[0]);
                    notificationMinute = Integer.parseInt(raw[1]);
                }
                else {
                    notificationHour = Integer.parseInt(raw[0]);
                    notificationMinute = Integer.parseInt(raw[1]);
                    String amPm = raw[2];

                }


                int prefTime = Integer.parseInt(preferenceTime);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                if (notificationTime == time - preferenceTime) {
                    createNotification(event);
                }
            }
        }

    }// startAlarms

    private void setNotificationAlarmTime() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
        String alarmTime = preferences.getString(application.getString(R.string.pref_alarm_time_key), " ");
        EventRepository repository = new EventRepository(application);
        repository.getAllEventsSortedByDate(Calendar.getInstance().getTime().toString());

        if (DateFormat.is24HourFormat(application)) {
            String[] rawTime = alarmTime.split("[: ]+");

        }
        else {

        }
    }// setNotificationAlarmTime


    // Method iterate through the list of entries and create notifications for them
    private void createNotification(@NonNull Event event) {

        final String GROUP_KEY_EVENTS = "to_do_list_group";
        int notificationCounter = 0;

        // Activity that will be launched
        Intent intent = new Intent(application, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent pendingIntent = PendingIntent.getActivity(application, 0, intent, 0);

        //for (Event entry : list) {

            // If this is a first notification, create notification summary with first notificaton
            if (notificationCounter == 0) {

                // Set notification summary group
                NotificationCompat.Builder summaryNotificationBuilder =
                        new NotificationCompat.Builder(application, application.getString(R.string.channel_id));
                summaryNotificationBuilder
                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
                        .setGroup(GROUP_KEY_EVENTS)
                        .setGroupSummary(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                // Create notification and add to notification summary group
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(application, application.getString(R.string.channel_id));
                notificationBuilder
                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
                        .setContentTitle(event.getTitle())
                        .setContentText(event.getDescription())
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setGroup(GROUP_KEY_EVENTS)
                        .setGroupSummary(false);

                // If API greater or equales to 26 add group alert behavior
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    summaryNotificationBuilder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);
                    notificationBuilder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);
                }

                // Send notification summary and notification
                notificationManager.notify(NOTIFICATION + NOTIFICATION, summaryNotificationBuilder.build());
                notificationManager.notify(NOTIFICATION + notificationCounter, notificationBuilder.build());

            } else {

                // Add notification to existed notification summary group
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(application, application.getString(R.string.channel_id));

                notificationBuilder.setContentTitle(event.getTitle())
                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
                        .setContentText(event.getDescription())
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setGroup(GROUP_KEY_EVENTS)
                        .setGroupSummary(false);

                // If API greater or equales to 26 add group alert behavior
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    notificationBuilder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);
                }

                notificationManager.notify(NOTIFICATION + notificationCounter, notificationBuilder.build());
            }

            notificationCounter++;

        //}// foreach

    }// createNotification

}// class
