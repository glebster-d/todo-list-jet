package com.glebsterd.mytodolist.helpers;

import android.os.AsyncTask;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventDao;

/**
 * AsyncTask that update event in database
 */
public class UpdateEventAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    /**
     * Constructor
     * @param eventDao application EventDao object
     */
    public UpdateEventAsyncTask(EventDao eventDao) {

        this.eventDao = eventDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(Event... events) {
        eventDao.updateEvent(events[0]);
        return null;
    }
}// class
