package com.glebsterd.mytodolist.helpers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventRepository;

import java.util.List;

/**
 *  ViewModel of application
 */
public class MainListViewModel extends AndroidViewModel {

    private final EventRepository repository;
    private final LiveData<List<Event>> eventListLiveData;

    /**
     * Constructor
     * @param application application context
     */
    public MainListViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        eventListLiveData = repository.getEventsList();
    }

    /**
     * Inserting new event to repository
     * @param event to add
     */
    public void insert(Event event){

        repository.insert(event);
    }

    /**
     * Getting all events from repository
     * @return LiveData object with all events
     */
    public LiveData<List<Event>> getAllEvents(){

        return eventListLiveData;
    }

    /**
     * Updating event
     * @param event to update
     */
    public void update(Event event) {

        repository.update(event);
    }

    /**
     * Deleting event from repository
     * @param event to delete
     */
    public void delete(Event event) {

        repository.delete(event);
    }

}// MainListViewModel.class
