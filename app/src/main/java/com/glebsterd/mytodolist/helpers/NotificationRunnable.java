package com.glebsterd.mytodolist.helpers;

import android.app.Application;

public class NotificationRunnable implements Runnable {

    private NotificationAlarmStarter alarmStarter;

    public NotificationRunnable(Application application) {

        alarmStarter = new NotificationAlarmStarter(application);
    }

    @Override
    public void run() {

        while (true) {

        }

    }

}// class
