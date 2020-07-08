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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class NotificationPusher {

    private static final String TAG = "NotificationPusher";

    private static final int NOTIFICATION = 30;

    private NotificationManagerCompat notificationManager;
    private final Application application;
    //private Calendar calendar;

    public NotificationPusher(Application application) {
        this.application = application;
    }

    public final void startAlarms() {

        notificationManager = NotificationManagerCompat.from(application);
        EventRepository eventRepository = new EventRepository(application);
        //calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        //String dateNow = java.text.DateFormat.getDateInstance().format(calendar.getTime());
        //String localDate = LocalDate.now().toString();
        //String localTime = LocalTime.now(ZoneId.systemDefault()).toString();

        //Log.d(TAG, "[StartAlarms] ---> dateFormat = " + dateNow);
        //Log.d(TAG, "[StartAlarms] ---> localDate = " + localDate);
        //Log.d(TAG, "[StartAlarms] ---> localTime = " + localTime);
        //Log.d(TAG, "[StartAlarms] ---> ZoneId = " + ZoneId.systemDefault());

        LocalTime localTimeNow = LocalTime.now(ZoneId.systemDefault());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(application);
        String[] preferenceReminderTime = pref.getString(application.getResources().getString(R.string.pref_alarm_time_key), "0 minutes").split(" ");

        String localDate = LocalDate.now().toString();
        List<Event> eventList = eventRepository.getAllEventsSortedByDate(localDate).getValue();

        if (eventList != null) {

            for (Event event: eventList) {

                int eventNotificationHour , eventNotificationMinute;
                String [] raw = event.getTime().split("[: ]+");

                if(DateFormat.is24HourFormat(application)) {

                    eventNotificationHour = Integer.parseInt(raw[0]);
                    eventNotificationMinute = Integer.parseInt(raw[1]);

                    int prefTime = Integer.parseInt(preferenceReminderTime[0]);

                    LocalTime eventTime = LocalTime.of(eventNotificationHour, eventNotificationMinute);
                    long duration = ChronoUnit.MINUTES.between(eventTime, localTimeNow);

                    if (duration == prefTime) {
                        createNotification(event);
                    }
                }
                else {

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

                    //DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("hh:mm a", DateTimeFormatter.)

                    eventNotificationHour = Integer.parseInt(raw[0]);
                    eventNotificationMinute = Integer.parseInt(raw[1]);
                    LocalTime eventTime = LocalTime.of(eventNotificationHour, eventNotificationMinute);
                    String amPm = raw[2];

                }
            }
        }

    }// startAlarms

//    private void setNotificationAlarmTime() {
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
//        String alarmTime = preferences.getString(application.getString(R.string.pref_alarm_time_key), " ");
//        EventRepository repository = new EventRepository(application);
//        repository.getAllEventsSortedByDate(Calendar.getInstance().getTime().toString());
//
//        if (DateFormat.is24HourFormat(application)) {
//            String[] rawTime = alarmTime.split("[: ]+");
//
//        }
//        else {
//
//        }
//    }// setNotificationAlarmTime


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
