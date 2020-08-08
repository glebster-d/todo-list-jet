package com.glebsterd.mytodolist.helpers;

import android.os.AsyncTask;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventDao;


public class AddEventAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    public AddEventAsyncTask(EventDao eventDao) {

        this.eventDao = eventDao;
    }

    @Override
    protected Void doInBackground(Event... events) {
        eventDao.insert(events[0]);
        return null;
    }
}
