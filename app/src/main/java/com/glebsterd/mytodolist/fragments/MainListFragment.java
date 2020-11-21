package com.glebsterd.mytodolist.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.helpers.EventListAdapter;
import com.glebsterd.mytodolist.helpers.ItemSwipeCallback;
import com.glebsterd.mytodolist.helpers.MainListViewModel;
import com.glebsterd.mytodolist.helpers.OperationMode;
import com.glebsterd.mytodolist.persistance.Event;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class MainListFragment extends Fragment implements EventListAdapter.OnEventClickListener {

    private static final String TAG = "MainListFragment";

    private ConstraintLayout constraintLayout;
    private EventListAdapter eventListAdapter;
    private AppCompatTextView emptyTextView;
    private AppCompatImageView emptyImage;
    private MainActivity parentActivity;
    private RecyclerView recyclerView;


    public MainListFragment() { }

    public static MainListFragment newInstance() {
        return new MainListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {

        Log.d(TAG, "[OnAttach] ---> IN");

        super.onAttach(context);
        parentActivity = (MainActivity) context;

        Log.d(TAG, "[OnAttach] ---> OUT");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreateView] ---> IN");
        return inflater.inflate(R.layout.fragment_main_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "[OnViewCreated] ---> IN");

        super.onViewCreated(view, savedInstanceState);

        MainListViewModel listViewModel = parentActivity.getViewModel();

        eventListAdapter = new EventListAdapter(this, parentActivity);

        setRecyclerView(view);
        setViews(view);

        listViewModel.getAllEvents().observe(getViewLifecycleOwner(), this::setVisibilityOfViews);
        enableSwipeToDeleteAndUndo();
    }

    private void setVisibilityOfViews(List<Event> events) {

        if(events == null || events.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyImage.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
        else {
            emptyImage.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            eventListAdapter.setEvents(events);
        }
    }

    private void setViews(@NonNull View view) {

        constraintLayout = view.findViewById(R.id.constraintLayout);
        emptyTextView = view.findViewById(R.id.tv_EmptyListLabel);
        emptyImage = view.findViewById(R.id.iv_EmptyListImg);
    }

    private void setRecyclerView(@NonNull View view) {

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {

        Log.d(TAG, "[OnResume] ---> IN");

        super.onResume();
        parentActivity.getFab().show();

        Log.d(TAG, "[OnResume] ---> OUT");
    }

    @Override
    public void onPause() {

        Log.d(TAG, "[OnPause] ---> IN");

        super.onPause();
        parentActivity.getFab().hide();

        Log.d(TAG, "[OnPause] ---> OUT");
    }

    @Override
    public void onEventClick(Event event) {

        EventOperationsFragment fragment = EventOperationsFragment.newInstance();
        setArguments(event, fragment);
        parentActivity.replaceFragment(fragment,null);
    }

    private void setArguments(Event event, EventOperationsFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode", OperationMode.EDIT);
        bundle.putString("title", event.getTitle());
        bundle.putString("description", event.getDescription());
        bundle.putString("date", event.getDate());
        bundle.putString("time", event.getTime());
        fragment.setArguments(bundle);
    }

    private void enableSwipeToDeleteAndUndo() {

        ItemSwipeCallback swipeToDeleteCallback = new ItemSwipeCallback(requireContext()) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Event item = eventListAdapter.getEvents().get(position);

                eventListAdapter.removeItem(position);
                parentActivity.getViewModel().delete(item);

                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);

                snackbar.setAction("UNDO", view -> {

                    eventListAdapter.restoreItem(item, position);
                    recyclerView.scrollToPosition(position);
                    parentActivity.getViewModel().insert(item);
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
