package com.glebsterd.mytodolist.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;

import java.util.HashMap;
import java.util.Objects;


public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    //private Preference timePreference;
    private DropDownPreference reminderTime;
    private ListPreference ringtonePreference;
    private MainActivity parentActivity;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(){ return new SettingsFragment(); }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adjustPreferences();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        //parentActivity.getFab().hide();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(R.string.settings);
    }

    @Override
    public void onPause(){
        super.onPause();
        //parentActivity.getFab().show();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(R.string.app_name);
    }

    @Override
    public void onStop() {
        super.onStop();
        parentActivity = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) context;
    }

    private void adjustPreferences() {
        boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(requireContext())
                                .getBoolean(getString(R.string.pref_alarm_check_key), true);

//        timePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_time_key));
//        Objects.requireNonNull(timePreference).setEnabled(isEnabled);
//        timePreference.setOnPreferenceClickListener(this);

        reminderTime = getPreferenceManager().findPreference(getString(R.string.pref_alarm_time_key));
        Objects.requireNonNull(reminderTime).setEnabled(isEnabled);
        reminderTime.setOnPreferenceClickListener(this);

        ringtonePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_sound_key));
        Objects.requireNonNull(ringtonePreference).setEnabled(isEnabled);
        ringtonePreference.setOnPreferenceClickListener(this);

        setListPreferenceData(ringtonePreference);

        CheckBoxPreference alarmEnablePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_check_key));
        Objects.requireNonNull(alarmEnablePreference).setOnPreferenceChangeListener(this);
    }

    private void setListPreferenceData(ListPreference ringtonePreference) {

        HashMap<String, Uri> alarms = getSystemAlarmRingtones();
        CharSequence[] listEntries = Objects.requireNonNull(alarms).keySet().toArray(new CharSequence[0]);
        Uri[] uriValues = alarms.values().toArray(new Uri[0]);
        CharSequence[] listEntryValues = new CharSequence[uriValues.length];

        for(int i = 0; i < uriValues.length; i++ ){

            listEntryValues[i] = uriValues[i].getPath();
        }

        ringtonePreference.setEntries(listEntries);
        ringtonePreference.setEntryValues(listEntryValues);
    }

    private HashMap<String, Uri> getSystemAlarmRingtones(){

        HashMap<String, Uri> ringtoneHashMap = new HashMap<>();

        RingtoneManager ringtoneManager = new RingtoneManager(parentActivity);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        int alarmsCount = cursor.getCount();



        if(alarmsCount == 0 && !cursor.moveToFirst()){
            return null;
        }

        while(!cursor.isAfterLast() && cursor.moveToNext()){

            int position = cursor.getPosition();

            String key = ringtoneManager.getRingtone(position).getTitle(getContext());
            Uri value = ringtoneManager.getRingtoneUri(position);

            ringtoneHashMap.put(key, value);
        }

        cursor.close();

        return ringtoneHashMap;
    }

    // -------------------DELETE AFTER TESTING---------------------
    @Override
    public boolean onPreferenceClick(Preference preference) {

        Log.d(TAG, "onPreferenceClick --> [Preference]: " + preference.getTitle());

        boolean clickHandled = false;

        if(preference.getKey().equals(getString(R.string.pref_alarm_sound_key))){

            String s = PreferenceManager.getDefaultSharedPreferences(requireContext())
                                        .getString(preference.getKey(), "Empty");
            Log.d(TAG, "onPreferenceClick --> [Preference]: " + preference.getTitle() + "[Value]: " + s);
            clickHandled = true;
        }

        if(preference.getKey().equals(getString(R.string.pref_alarm_time_key))){

            clickHandled = true;
        }

        return clickHandled;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean preferenceUpdated = false;

        Log.d(TAG, "OnPreferenceChange Method ---> [Preference]: " + preference.getTitle()
                + " [Value]: "+ newValue.toString());

        if(preference.getKey().equals(getString(R.string.pref_alarm_check_key))){

            ringtonePreference.setEnabled(Boolean.parseBoolean(newValue.toString()));
            reminderTime.setEnabled(Boolean.parseBoolean(newValue.toString()));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            sharedPreferences.edit().putBoolean(getString(R.string.pref_alarm_check_key),
                    Boolean.parseBoolean(newValue.toString())).commit();

            preferenceUpdated = true;
        }

        return preferenceUpdated;
    }

}// SettingsFragment.class
