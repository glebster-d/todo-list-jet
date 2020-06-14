package com.glebsterd.mytodolist.helpers;

import android.app.Application;

public class NotificationRunnable implements Runnable {

    private NotificationAlarmStarter alarmStarter;

    public NotificationRunnable(Application application) {

        alarmStarter = new NotificationAlarmStarter(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        while (true) {


            alarmStarter.startAlarms();
            goToSleep();
        }
    }

    private void goToSleep() {

        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}// class
