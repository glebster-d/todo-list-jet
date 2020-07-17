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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;


/**
 * Dialog that creates DatePicker
 */
public class EventDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "EventDateDialog";

    private int day, month, year;

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreateDialog] ---> IN");

        if (savedInstanceState != null){
            day = savedInstanceState.getInt("day");
            month= savedInstanceState.getInt("month");
            year = savedInstanceState.getInt("year");
        }
        else{
            LocalDate localDate = getLocalDateFromArguments();
            day = localDate.getDayOfMonth();
            month = localDate.getMonthValue();
            year = localDate.getYear();
        }

        return new DatePickerDialog(requireActivity(), this, year, month - 1, day);

    }// onCreateDialog

    private LocalDate getLocalDateFromArguments() {

        Bundle args = getArguments();

        return ( args != null ) ?
                LocalDate.parse(args.getString("date")) : LocalDate.now(ZoneId.systemDefault());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Log.d(TAG, "[OnDataSet Method] ---> IN ");
        Log.d(TAG, "OnSet.   Year = " + year + " month= "+ (month )+" day= " + day);

        DialogFragmentListener listener = (DialogFragmentListener) getTargetFragment();
        LocalDate localDate = LocalDate.of(year,month + 1,day);
        Objects.requireNonNull(listener).onFinishEditingDialog(this.getClass().getSimpleName(), localDate.toString());

        Log.d(TAG, "[OnDataSet Method] ---> OUT ");

    }// onDateSet

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt("day", day);
        outState.putInt("month", month);
        outState.putInt("year", year);

    }// onSaveInstanceState

}// EventDateDialog.class
