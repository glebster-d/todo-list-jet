package com.glebsterd.mytodolist.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventRepository;

import java.util.List;

/**
 * Service that runs every day and checks database enties. If current date and date of entry
 * are the same, service will trigger notification.
 */
public class NotificationService extends Service {

    private static final int NOTIFICATION = 30;
    private NotificationManagerCompat notificationManager;
    private EventRepository eventRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {

        eventRepository = new EventRepository(getApplication());
        List<Event> eventList = eventRepository.getEventsList().getValue();


        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
        //Date currentDate = new Date();
        //String dateString = dateFormat.format(currentDate);
        //List<ListEntry> list = dbTool.getAllEntries(dateString);

        // If we don't have events for that day, stop service, otherwise create notification
//        if (list != null) {
//
//            createNotification(list);
//        }

        stopSelf();

    }// onCreate

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }// onStartCommand



    // Method iterate through the list of entries and create notifications for them
    private void createNotification(List<Event> list) {

        final String GROUP_KEY_EVENTS = "to_do_list_group";
        int notificationCounter = 0;

        // Activity that will be launched
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        for (ListEntry entry : list) {
//
//            // If this is a first notification, create notification summary with first notificaton
//            if (notificationCounter == 0) {
//
//                // Set notification summary group
//                NotificationCompat.Builder summaryNotificationBuilder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
//                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
//                        .setGroup(GROUP_KEY_EVENTS)
//                        .setGroupSummary(true)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true);
//
//                // Create notification and add to notification summary group
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
//                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
//                        .setContentTitle(entry.getTitle())
//                        .setContentText(entry.getDescription())
//                        .setPriority(NotificationCompat.PRIORITY_LOW)
//                        .setGroup(GROUP_KEY_EVENTS)
//                        .setGroupSummary(false);
//
//                // If API greater or equales to 26 add group alert behavior
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    summaryNotificationBuilder.setGroupAlertBehavior(Notification.GROUP_ALERT_SUMMARY);
//                    notificationBuilder.setGroupAlertBehavior(Notification.GROUP_ALERT_SUMMARY);
//                }
//
//                // Send notification summary and notification
//                notificationManager.notify(NOTIFICATION + NOTIFICATION, summaryNotificationBuilder.build());
//                notificationManager.notify(NOTIFICATION + notificationCounter, notificationBuilder.build());
//
//            } else {
//
//                // Add notification to existed notification summary group
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
//                        .setSmallIcon(R.drawable.ic_stat_notifications_todolist)
//                        .setContentTitle(entry.getTitle())
//                        .setContentText(entry.getDescription())
//                        .setPriority(NotificationCompat.PRIORITY_LOW)
//                        .setGroup(GROUP_KEY_EVENTS)
//                        .setGroupSummary(false);
//
//                // If API greater or equales to 26 add group alert behavior
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    notificationBuilder.setGroupAlertBehavior(Notification.GROUP_ALERT_SUMMARY);
//                }
//
//                notificationManager.notify(NOTIFICATION + notificationCounter, notificationBuilder.build());
//            }
//
//            notificationCounter++;
//
//        }// foreach

    }// createNotification

}// class
