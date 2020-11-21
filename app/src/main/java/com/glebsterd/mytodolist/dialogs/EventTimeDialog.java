package com.glebsterd.mytodolist.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.glebsterd.mytodolist.helpers.DialogFragmentListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class EventTimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int hour, minute;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        restoreFromSavedState(savedInstanceState);

        return new TimePickerDialog(getActivity(),this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    private void restoreFromSavedState(@Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            hour = savedInstanceState.getInt("hour");
            minute = savedInstanceState.getInt("minute");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        LocalTime localTime = LocalTime.of(hourOfDay, minute);

        DateTimeFormatter timeFormatter = (DateFormat.is24HourFormat(getContext())) ?
                DateTimeFormatter.ofPattern("HH:mm"):
                DateTimeFormatter.ofPattern("hh:mm a");

        DialogFragmentListener listener = (DialogFragmentListener) getTargetFragment();
        Objects.requireNonNull(listener)
                .onFinishEditingDialog(this.getClass().getSimpleName(), localTime.format(timeFormatter));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt("hour", hour);
        outState.putInt("minute", minute);
    }
}
