package com.glebsterd.mytodolist.persistance;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.glebsterd.mytodolist.helpers.AddEventAsyncTask;
import com.glebsterd.mytodolist.helpers.DeleteEventAsyncTask;
import com.glebsterd.mytodolist.helpers.UpdateEventAsyncTask;

import java.util.List;

public class EventRepository {

    private final EventDao eventDao;
    private final LiveData<List<Event>> eventsList;

    public EventRepository(Application application) {
        EventDatabase db = EventDatabase.getDatabase(application);
        this.eventDao = db.eventDao();
        this.eventsList = eventDao.getAllEvents();
    }

    public void insert(Event event) {

        new AddEventAsyncTask(eventDao).execute(event);
    }

    public void update(Event event) {

        new UpdateEventAsyncTask(eventDao).execute(event);
    }

    public void delete(Event event) {

        new DeleteEventAsyncTask(eventDao).execute(event);
    }

    public LiveData<List<Event>> getEventsList() {
        return eventsList;
    }

    public LiveData<List<Event>> getAllEventsSortedByDate(@NonNull String date) {

        return eventDao.getAllEventsSortedByDate(date);
    }

}// EventRepository.class
