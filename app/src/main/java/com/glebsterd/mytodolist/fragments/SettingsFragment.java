package com.glebsterd.mytodolist.fragments;


import android.content.Context;
import android.content.Intent;
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

;
import com.glebsterd.mytodolist.R;
import com.glebsterd.mytodolist.activity.MainActivity;
import com.glebsterd.mytodolist.receivers.ServiceStarterReceiver;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    public static final String BROADCAST_ACTION = "com.glebsterd.mytodolist.SETTINGS_CHANGED";

    private DropDownPreference reminderTimePreference;
    private ListPreference ringtonePreference;
    private MainActivity parentActivity;

    public SettingsFragment() {}

    public static SettingsFragment newInstance(){ return new SettingsFragment(); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adjustPreferences();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(R.string.settings);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause(){
        super.onPause();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(R.string.app_name);
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        super.onStop();
        parentActivity = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) context;
    }

    private void adjustPreferences() {
        boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(requireContext())
                                .getBoolean(getString(R.string.pref_alarm_check_key), true);

        reminderTimePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_time_key));
        Objects.requireNonNull(reminderTimePreference).setEnabled(isEnabled);

        String summary = getSummaryString(R.string.pref_alarm_time_summary, reminderTimePreference.getValue());
        reminderTimePreference.setSummary(summary);

        ringtonePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_sound_key));
        Objects.requireNonNull(ringtonePreference).setEnabled(isEnabled);

        setListPreferenceData(ringtonePreference);

        summary = getSummaryString(R.string.pref_alarm_sound_summary, ringtonePreference.getEntry().toString());
        ringtonePreference.setSummary(summary);

        CheckBoxPreference alarmEnablePreference = getPreferenceManager().findPreference(getString(R.string.pref_alarm_check_key));
        Objects.requireNonNull(alarmEnablePreference).setOnPreferenceChangeListener(this);

    }// adjustPreferences

    private String getSummaryString(int stringId, String value) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(getString(stringId))
                .append("\t\t\t")
                .append(value)
                .toString();

    }// getSummaryString

    // Setting data to list preference
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

    }// setListPreferenceData

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

    }// getSystemAlarmRingtones

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean preferenceUpdated = false;

        Log.d(TAG, "OnPreferenceChange Method ---> [Preference]: " + preference.getTitle()
                + " [Value]: "+ newValue.toString());

        if(preference.getKey().equals(getString(R.string.pref_alarm_check_key))){

            ringtonePreference.setEnabled(Boolean.parseBoolean(newValue.toString()));
            reminderTimePreference.setEnabled(Boolean.parseBoolean(newValue.toString()));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            sharedPreferences.edit().putBoolean(getString(R.string.pref_alarm_check_key),
                    Boolean.parseBoolean(newValue.toString())).apply();

            Intent intent = new Intent(requireContext(), ServiceStarterReceiver.class);
            intent.setAction(BROADCAST_ACTION);
            getActivity().sendBroadcast(intent);

            preferenceUpdated = true;
        }

        return preferenceUpdated;

    }// onPreferenceChange

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String summary;

        if(key.equals(getString(R.string.pref_alarm_time_key))) {
            String newValue = sharedPreferences.getString(key, getString(R.string.pref_alarm_time_default));
            summary = getSummaryString(R.string.pref_alarm_time_summary, newValue);
            reminderTimePreference.setSummary(summary);
        }
        else if (key.equals(getString(R.string.pref_alarm_sound_key))) {
            summary = getSummaryString(R.string.pref_alarm_sound_summary, ringtonePreference.getEntry().toString());
            ringtonePreference.setSummary(summary);
        }
    }// onSharedPreferenceChanged

}// SettingsFragment.class
