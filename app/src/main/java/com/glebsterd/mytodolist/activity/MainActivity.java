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


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FloatingActionButton fab;
    private MainListViewModel listViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "[OnCreate] ---> IN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setFloatingActionButton();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        listViewModel = new ViewModelProvider(this).get(MainListViewModel.class);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, MainListFragment.newInstance(), MainListFragment.class.getSimpleName())
                .commit();

        Log.d(TAG, "[OnCreate] ---> OUT");
    }

    private void setFloatingActionButton() {

        fab = findViewById(R.id.fab_MainActivity);
        fab.setOnClickListener(view -> replaceFragment(EventOperationsFragment.newInstance(), null));
    }

    private void setToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            replaceFragment(SettingsFragment.newInstance(), SettingsFragment.class.getSimpleName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(@NonNull Fragment replacement, @Nullable String tag){

        Log.d(TAG, "[ReplaceFragment] ---> IN");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, replacement)
                .addToBackStack(tag)
                .commit();
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public MainListViewModel getViewModel() {
        return listViewModel;
    }
}
