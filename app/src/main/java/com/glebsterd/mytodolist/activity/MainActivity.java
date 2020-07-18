package com.glebsterd.mytodolist.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.fragments.EventOperationsFragment;
import com.glebsterd.mytodolist.fragments.MainListFragment;
import com.glebsterd.mytodolist.fragments.SettingsFragment;
import com.glebsterd.mytodolist.helpers.MainListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Main activity for application
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FloatingActionButton fab;
    private MainListViewModel listViewModel;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreate] ---> IN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab_MainActivity);

        // When FAB clicked replace MainListFragment with EventOperationsFragment
        fab.setOnClickListener(view -> replaceFragment(EventOperationsFragment.newInstance(), null));

        // Setting default preferences from the XML file. Only called once by setting readAgain to false.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Getting main view model for events list
        listViewModel = new ViewModelProvider(this).get(MainListViewModel.class);

        // Adding fragment with events list
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, MainListFragment.newInstance(), MainListFragment.class.getSimpleName())
                .commit();

        Log.d(TAG, "[OnCreate] ---> OUT");

    }// onCreate

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }// onCreateOptionsMenu

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            replaceFragment(SettingsFragment.newInstance(), SettingsFragment.class.getSimpleName());
            return true;
        }

        return super.onOptionsItemSelected(item);

    }// onOptionsItemSelected

    /**
     * Replace current fragment with another
     * @param replacement new fragment
     * @param tag fragment tag string for back stack or null
     */
    public void replaceFragment(@NonNull Fragment replacement, @Nullable String tag){

        Log.d(TAG, "[ReplaceFragment] ---> IN");
//
//        if (tag.equals(SettingsFragment.class.getSimpleName())) {
//
//            //setTitle(getString(R.string.settings));
//        }
//        else if (tag.equals(MainListFragment.class.getSimpleName())) {
//
//            setTitle(getString(R.string.app_name));
//        }
//        else if (tag.equals(EventOperationsFragment.class.getSimpleName())) {
//
//            setTitle(getString(R.string.add_edit_event));
//        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, replacement)
                .addToBackStack(tag)
                .commit();

    }// replaceFragment

    /**
     * Get a floating action button
     * @return FAB of main activity
     */
    public FloatingActionButton getFab() {
        return fab;
    }

    /**
     * Get a view model
     * @return main view model object
     */
    public MainListViewModel getViewModel() {
        return listViewModel;
    }

}// MainActivity.class
