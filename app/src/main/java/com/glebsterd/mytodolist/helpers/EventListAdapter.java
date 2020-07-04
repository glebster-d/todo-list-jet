package com.glebsterd.mytodolist.helpers;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.persistance.Event;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private static final String TAG = "EventListAdapter";

    private List<Event> events;
    private final OnEventClickListener eventClickListener;
    private Context context;

    public interface OnEventClickListener {

        void onEventClick(Event event);
    }

    public EventListAdapter(OnEventClickListener eventClickListener, Context context) {

        this.eventClickListener = eventClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View eventView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_item, parent, false);
        return new EventViewHolder(eventView, eventClickListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        if (events != null){

            Event event = events.get(position);
            holder.setData(event);
        }
    }

    @Override
    public int getItemCount() {
        return (events == null) ? 0 : events.size();
    }

    public void setEvents(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }


     static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final AppCompatTextView tvTitle;
        private final AppCompatTextView tvDate;
        private final AppCompatTextView tvDescription;
        private final AppCompatTextView tvTime;
        private final OnEventClickListener eventClickListener;
        private final Context context;

        EventViewHolder(@NonNull View itemView, OnEventClickListener eventClickListener, Context context) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDescription = itemView.findViewById(R.id.tv_description);

            this.eventClickListener = eventClickListener;
            this.context = context;
            itemView.setOnClickListener(this);
        }

        void setData(@NonNull Event event){

            //Log.d(TAG, "SetData Method entrance. Event: " + event);
            tvDate.setText(event.getDate());
            String time = getFormattedTimeFromString(event.getTime());
            //tvTime.setText(event.getTime());
            tvTime.setText(time);
            tvTitle.setText(event.getTitle());
            tvDescription.setText(event.getDescription());
        }

        private String getFormattedTimeFromString(String time) {

            String formattedTimeString = "";
            LocalTime formattedTime;

            if (DateFormat.is24HourFormat(context)) {

                formattedTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
                formattedTimeString = formattedTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            else {
                formattedTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
                formattedTimeString = formattedTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
            }

            return formattedTimeString;
        }

        @Override
        public void onClick(View v) {

            String title = tvTitle.getText().toString();
            String description = tvDescription.getText().toString();
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();

            Event event = new Event(date,time,title,description);
            eventClickListener.onEventClick(event);
        }

    }// EventViewHolder.class

}// EventListAdapter.class
