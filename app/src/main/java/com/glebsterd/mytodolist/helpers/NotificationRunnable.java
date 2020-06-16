package com.glebsterd.mytodolist.helpers;

import android.app.Application;

public class NotificationRunnable implements Runnable {

    private NotificationPusher notificationPusher;

    public NotificationRunnable(Application application) {

        notificationPusher = new NotificationPusher(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        while (true) {


            notificationPusher.startAlarms();
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
