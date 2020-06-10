package com.glebsterd.mytodolist.helpers;

import android.os.AsyncTask;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventDao;

public class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    public DeleteEventAsyncTask(EventDao eventDao) {

        this.eventDao = eventDao;
    }

    @Override
    protected Void doInBackground(Event... events) {

        eventDao.deleteEvent(events[0]);
        return  null;
    }
}
