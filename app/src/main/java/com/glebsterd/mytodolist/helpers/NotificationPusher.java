package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public final class NotificationPusher {

    private static final String TAG = "NotificationPusher";

    private static final int NOTIFICATION = 30;

    private NotificationManagerCompat notificationManager;
    private final Application application;
    private List<Event> eventList;
    private String dateNow = "";


    /**
     * Constructor
     * @param application application context
     */
    public NotificationPusher(Application application) {
        this.application = application;
    }

    public final void pushNotifications() {

        int notificationCounter = 0;

        notificationManager = NotificationManagerCompat.from(application);
        EventRepository eventRepository = new EventRepository(application);

        LocalTime localTimeNow = LocalTime.now(ZoneId.systemDefault());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(application);
        String[] preferenceReminderTime =
                Objects.requireNonNull(pref.getString(application.getResources()
                        .getString(R.string.pref_alarm_time_key), "0 minutes"))
                        .split(" ");

        String localDate = LocalDate.now().toString();

        if (!dateNow.equals(localDate)) {
            dateNow = localDate;
            eventList = eventRepository.getAllEventsSortedByDate(localDate);
        }

        if (eventList != null) {

            for (Event event: eventList) {

                LocalTime eventTime = getFormattedEventTime(event.getTime());
                long duration = ChronoUnit.MINUTES.between(localTimeNow, eventTime);
                int prefTime = Integer.parseInt(preferenceReminderTime[0]);

                if (duration == prefTime) {
                    createNotification(event, notificationCounter);
                    notificationCounter++;
                }

            }// foreach
        }

    }// startAlarms

    private LocalTime getFormattedEventTime(String time) {

        DateTimeFormatter timeFormatter = null;

        Pattern patter24HourFormat = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        Matcher matcher24HourFormat = patter24HourFormat.matcher(time);

        Pattern pattern12HourFormat = Pattern.compile("([01][012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)");
        Matcher matcher12HourFormat = pattern12HourFormat.matcher(time);

        if(matcher12HourFormat.find()) {
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        }
        else if (matcher24HourFormat.find()) {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        }

        return  LocalTime.parse(time, timeFormatter);

    }// getFormattedEventTime

    // Method iterate through the list of entries and create notifications for them
    private void createNotification(@NonNull Event event, int notificationCounter) {

        final String GROUP_KEY_EVENTS = "to_do_list_group";

        // Activity that will be launched
        Intent intent = new Intent(application, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent pendingIntent = PendingIntent.getActivity(application, 0, intent, 0);

        // If this is a first notification, create notification summary with first notification
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

            // If API greater or equals to 26 add group alert behavior
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

            // If API greater or equals to 26 add group alert behavior
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notificationBuilder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);
            }

            notificationManager.notify(NOTIFICATION + notificationCounter, notificationBuilder.build());
        }

    }// createNotification

}// class
