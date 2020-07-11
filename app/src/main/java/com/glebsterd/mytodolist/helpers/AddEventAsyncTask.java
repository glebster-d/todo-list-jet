package com.glebsterd.mytodolist.helpers;

import android.os.AsyncTask;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventDao;

/**
 * AsyncTask that add event to database
 */
public class AddEventAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    /**
     * Constructor
     * @param eventDao application EventDao object
     */
    public AddEventAsyncTask(EventDao eventDao) {

        this.eventDao = eventDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(Event... events) {
        eventDao.insert(events[0]);
        return null;
    }

}// InsertEventAsyncTask.class
