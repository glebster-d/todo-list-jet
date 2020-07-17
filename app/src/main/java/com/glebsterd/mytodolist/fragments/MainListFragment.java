package com.glebsterd.mytodolist.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.helpers.EventListAdapter;
import com.glebsterd.mytodolist.helpers.MainListViewModel;
import com.glebsterd.mytodolist.helpers.OperationMode;
import com.glebsterd.mytodolist.persistance.Event;

/**
 * Main fragment with events list
 */
public class MainListFragment extends Fragment implements EventListAdapter.OnEventClickListener{

    private static final String TAG = "MainListFragment";

    private EventListAdapter eventListAdapter;
    private AppCompatTextView emptyTextView;
    private AppCompatImageView emptyImage;
    private RecyclerView recyclerView;
    private MainActivity parentActivity;

    /**
     * Constructor
     */
    public MainListFragment() {
        // Required empty public constructor
    }

    /**
     * Get an instance of MainListFragment
     *
     * @return instance of this fragment
     */
    public static MainListFragment newInstance() {
        return new MainListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context) {

        Log.d(TAG, "[OnAttach] ---> IN");

        super.onAttach(context);
        parentActivity = (MainActivity) context;

        Log.d(TAG, "[OnAttach] ---> OUT");

    }// onAttach

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreateView] ---> IN");
        return inflater.inflate(R.layout.fragment_main_recycle_view, container, false);

    }// onCreateView

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "[OnViewCreated] ---> IN");

        super.onViewCreated(view, savedInstanceState);

        MainListViewModel listViewModel = parentActivity.getViewModel();

        eventListAdapter = new EventListAdapter(this, parentActivity);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyTextView = view.findViewById(R.id.tv_EmptyListLabel);
        emptyImage = view.findViewById(R.id.iv_EmptyListImg);

        listViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {

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
        });
    }// onViewCreated

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {

        Log.d(TAG, "[OnResume] ---> IN");

        super.onResume();
        parentActivity.getFab().show();

        Log.d(TAG, "[OnResume] ---> OUT");

    }// onResume

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {

        Log.d(TAG, "[OnPause] ---> IN");

        super.onPause();
        parentActivity.getFab().hide();

        Log.d(TAG, "[OnPause] ---> OUT");

    }// onPause

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEventClick(Event event) {

        EventOperationsFragment fragment = EventOperationsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode", OperationMode.EDIT);
        bundle.putString("title", event.getTitle());
        bundle.putString("description", event.getDescription());
        bundle.putString("date", event.getDate());
        bundle.putString("time", event.getTime());
        fragment.setArguments(bundle);

        parentActivity.replaceFragment(fragment,null);

    }// onEventClick

}// MainListFragment.class
