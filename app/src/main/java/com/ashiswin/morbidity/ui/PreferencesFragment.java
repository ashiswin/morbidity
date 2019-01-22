package com.ashiswin.morbidity.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.ashiswin.morbidity.utils.DatePreference;
import com.ashiswin.morbidity.DatePreferenceDialogFragmentCompat;
import com.ashiswin.morbidity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends PreferenceFragmentCompat {
    private final String TAG = getClass().getSimpleName();

    SharedPreferences sharedPreferences;
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
        sharedPreferences = getPreferenceManager().getSharedPreferences();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof DatePreference) {
            dialogFragment = new DatePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final EditTextPreference editTextPreference = (EditTextPreference) findPreference("name");
        editTextPreference.setSummary(sharedPreferences.getString("name", sharedPreferences.getString("name", "No name set")));
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

            String yourString = o.toString();
            sharedPreferences.edit().putString("name", yourString).apply();
            editTextPreference.setSummary(yourString);

            return true;
            }
        });
    }
}