package com.glebsterd.mytodolist.helpers;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.glebsterd.mytodolist.persistance.Event;
import com.glebsterd.mytodolist.persistance.EventRepository;

import java.util.List;


public class MainListViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();

    private final EventRepository repository;
    private final LiveData<List<Event>> eventListLiveData;

    public MainListViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
        eventListLiveData = repository.getEventsList();
    }

    public void insert(Event event){

        repository.insert(event);
    }

    public LiveData<List<Event>> getAllEvents(){

        return eventListLiveData;
    }

    public void update(Event event) {

        repository.update(event);
    }

    public void delete(Event event) {

        repository.delete(event);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ViewModel Cleared!");
    }

}// MainListViewModel.class
