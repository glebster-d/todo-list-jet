package com.glebsterd.mytodolist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.dialogs.EventDateDialog;
import com.glebsterd.mytodolist.dialogs.EventTimeDialog;
import com.glebsterd.mytodolist.helpers.DialogFragmentListener;
import com.glebsterd.mytodolist.helpers.OperationMode;
import com.glebsterd.mytodolist.persistance.Event;

import java.util.Objects;

public class EventOperationsFragment extends Fragment implements View.OnClickListener, DialogFragmentListener {

    private static final String TAG = "EventOperationsFragment";



    private MainActivity parentActivity;
    private AppCompatEditText etTitle, etDescription;
    private AppCompatTextView etTime, etDate;
    private OperationMode mode = OperationMode.ADD;

    private String title = "", description = "", time = "", date = "";

    public EventOperationsFragment() {
        // Required empty public constructor
    }

    public static EventOperationsFragment newInstance(){

        return new EventOperationsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "[OnAttach Method] ---> IN ");
        super.onAttach(context);

        parentActivity = (MainActivity) context;
        Log.d(TAG, "[OnAttach Method] ---> OUT ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = (mode == OperationMode.ADD) ?
                getString(R.string.fragment_add_title) : getString(R.string.fragment_edit_title);
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(title);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(R.string.app_name);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void restoreViewData(Bundle savedInstanceState) {

        Log.d(TAG, "[RestoreViewData Method] ---> IN ");

        title = savedInstanceState.getString("title");
        description = savedInstanceState.getString("description");
        time = savedInstanceState.getString("time");
        date = savedInstanceState.getString("date");

        etTitle.setText(title);
        etDescription.setText(description);
        etTime.setText(time);
        etDate.setText(date);

        Log.d(TAG, "[RestoreViewData Method] ---> OUT ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreateView Method] ---> IN");
        return inflater.inflate(R.layout.fragment_event_operations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "[OnViewCreated Method] ---> IN");
        super.onViewCreated(view, savedInstanceState);
        adjustViews(view);

        if(savedInstanceState != null){

            restoreViewData(savedInstanceState);
        }

        Bundle args = getArguments();

        if (args != null){

            mode = (OperationMode) args.getSerializable("mode");
            restoreViewData(args);
        }

        Log.d(TAG, "[OnViewCreated Method] ---> OUT");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "[OnSaveInstanceState Method] ---> IN ");
        super.onSaveInstanceState(outState);

        if(title != null){ outState.putString("title", title); }
        if(description != null){ outState.putString("description", description); }
        if(date != null){ outState.putString("date", date); }
        if(time != null){ outState.putString("time", time); }
        Log.d(TAG, "[OnSaveInstanceState Method] ---> OUT ");
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "[OnClick Method] ---> IN ");

        switch(view.getId()){

            case R.id.et_EventDate:
                EventDateDialog dateDialog = new EventDateDialog();

                if (mode == OperationMode.EDIT){

                    Bundle args = new Bundle();
                    args.putString("date", date);
                    dateDialog.setArguments(args);
                }

                dateDialog.setTargetFragment(this, 0);
                dateDialog.show(getParentFragmentManager(),"DATE_DIALOG");
                break;

            case R.id.et_EventTime:
                EventTimeDialog timeDialog = new EventTimeDialog();

                if (mode == OperationMode.EDIT){

                    Bundle args = new Bundle();
                    args.putString("time", time);
                    timeDialog.setArguments(args);
                }

                timeDialog.setTargetFragment(this, 0);
                timeDialog.show(getParentFragmentManager(), "TIME_DIALOG");
                break;

        }
        Log.d(TAG, "[OnClick Method] ---> OUT ");
    }

    private void savingNewEvent(View view) {

        Log.d(TAG, "[SavingNewEvent Method] ---> IN ");
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String title = Objects.requireNonNull(etTitle.getText()).toString();
        String description = Objects.requireNonNull(etDescription.getText()).toString();

        Log.d(TAG, "[SavingNewEvent Method] ---> [title]: " + title);

        if (isValidEventInput(title, date, time)) {
            saveEvent(title, date, time, description);
        }
        else {
            Toast.makeText(parentActivity, getString(R.string.cannot_save_empty_event), Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "[SavingNewEvent Method] ---> OUT ");
    }

    private void saveEvent(String title, String date, String time, String description) {

        switch (mode) {

            case ADD:
                parentActivity.getViewModel().insert(new Event(date, time, title, description));
                parentActivity.getSupportFragmentManager().popBackStack();
                Toast.makeText(parentActivity, getString(R.string.event_was_added), Toast.LENGTH_LONG).show();
                break;

            case EDIT:
                parentActivity.getViewModel().update(new Event(date, time, title, description));
                parentActivity.getSupportFragmentManager().popBackStack();
                Toast.makeText(parentActivity, getString(R.string.event_was_updated), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private boolean isValidEventInput(String title, String date, String time) {
        return !time.trim().isEmpty() && !date.trim().isEmpty() && !title.trim().isEmpty();
    }

    private void adjustViews(@NonNull View view){

        etTitle = view.findViewById(R.id.et_EventTitle);
        etDescription = view.findViewById(R.id.et_EventDescription);
        etDate = view.findViewById(R.id.et_EventDate);
        etDate.setKeyListener(null);
        etDate.setOnClickListener(this);
        etTime = view.findViewById(R.id.et_EventTime);
        etTime.setKeyListener(null);
        etTime.setOnClickListener(this);
        AppCompatButton btnSave = view.findViewById(R.id.btn_Save);
        btnSave.setOnClickListener(this::savingNewEvent);
        AppCompatButton btnCancel = view.findViewById(R.id.btn_Cancel);
        btnCancel.setOnClickListener(v -> parentActivity.onBackPressed());
    }

    @Override
    public void onFinishEditingDialog(@NonNull String dialogName, @NonNull String data) {

        switch (dialogName){

            case "EventTimeDialog":
                etTime.setText(data);
                break;

            case "EventDateDialog":
                etDate.setText(data);
                break;
        }
    }

}// EventOperationsFragment.class