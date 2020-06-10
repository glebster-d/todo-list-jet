package com.glebsterd.mytodolist.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.glebsterd.mytodolist.helpers.DialogFragmentListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class EventDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "EventDateDialog";

    private int day, month, year;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "OnCreateDialog Method entrance");

        if (savedInstanceState != null){
            day = savedInstanceState.getInt("day");
            month= savedInstanceState.getInt("month");
            year = savedInstanceState.getInt("year");
        }
        else{
            Calendar calendar = getCalendarFromArguments();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }

        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    private Calendar getCalendarFromArguments() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        Bundle args = getArguments();

        String dateString = ( args != null ) ?
                args.getString("date") :
                dateFormat.format(Calendar.getInstance().getTime());

        if (dateString != null) {
            
            try {
                Date date = dateFormat.parse(dateString);
                calendar.setTime(date != null ? date : new Date());

            } catch (ParseException e) {
                e.printStackTrace();                
            }            
        }

        return calendar;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Log.d(TAG, "[OnDataSet Method] ---> IN ");
        Log.d(TAG, "OnSet.   Year = " + year + " month= "+ (month )+" day= " + day);

        DialogFragmentListener listener = (DialogFragmentListener) getTargetFragment();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.set(year,month,day);
        String stringDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        Objects.requireNonNull(listener).onFinishEditingDialog(this.getClass().getSimpleName(), stringDate);
        Log.d(TAG, "[OnDataSet Method] ---> OUT ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("day", day);
        outState.putInt("month", month);
        outState.putInt("year", year);
    }

}// EventDateDialog.class
