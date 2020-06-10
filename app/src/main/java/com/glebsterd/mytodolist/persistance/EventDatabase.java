package com.glebsterd.mytodolist.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.glebsterd.mytodolist.R;

/*
* No migration strategy added. Must add migration strategy
* */
@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase eventDatabase;

    public abstract EventDao eventDao();

    public static synchronized EventDatabase getDatabase(final Context context){

        if (eventDatabase == null){

            synchronized (EventDatabase.class){
                if (eventDatabase == null){
                    eventDatabase = Room
                            .databaseBuilder(context.getApplicationContext(),
                                             EventDatabase.class,
                                             context.getResources().getString(R.string.db_name))
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return eventDatabase;
    }

}// EventDatabase.class