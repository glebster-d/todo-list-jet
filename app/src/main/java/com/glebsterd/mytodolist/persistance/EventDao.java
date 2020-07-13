package com.glebsterd.mytodolist.persistance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data access object for events manipulation
 */
@Dao
public interface EventDao {

    @Insert
    void insert(@NonNull Event event);

    @Query("SELECT * FROM my_events")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM my_events WHERE event_date=:date")
    List<Event> getAllEventsSortedByDate(@NonNull String date);

    @Update
    void updateEvent(@NonNull Event event);

    @Delete
    void deleteEvent(@NonNull Event event);

}// interface