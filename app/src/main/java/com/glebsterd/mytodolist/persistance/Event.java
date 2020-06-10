package com.glebsterd.mytodolist.persistance;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "my_events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    private long id;

    @NonNull
    @ColumnInfo(name = "event_date")
    private final String date;

    @NonNull
    @ColumnInfo(name = "event_time")
    private final String time;

    @NonNull
    @ColumnInfo(name = "event_title")
    private final String title;

    @NonNull
    @ColumnInfo(name = "event_description")
    private String description = "";


    //      ------ Constructors ------

    @Ignore
    public Event(@NonNull String date, @NonNull String time ,@NonNull String title) {

        this.date = date;
        this.time = time;
        this.title = title;
    }

    public Event(@NonNull String date, @NonNull String time ,@NonNull String title, @NonNull String description) {

        this(date, time,title);
        this.description = description;
    }

    //      ------ Getters / Setters ------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getTime(){ return time; }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}// Event.class