package com.glebsterd.mytodolist.helpers;

import android.os.AsyncTask;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventDao;

/**
 *  AsyncTask that deletes event from database
 */
public class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    /**
     * Constructor
     * @param eventDao application EventDao object
     */
    public DeleteEventAsyncTask(EventDao eventDao) {

        this.eventDao = eventDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(Event... events) {

        eventDao.deleteEvent(events[0]);
        return  null;
    }
}// class
