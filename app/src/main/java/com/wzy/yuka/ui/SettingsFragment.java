package com.wzy.yuka.ui;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.wzy.yuka.R;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        getPreferenceScreen().findPreference("about_Yuka").setOnPreferenceClickListener(this);
        getPreferenceScreen().findPreference("about_dev").setOnPreferenceClickListener(this);
        getPreferenceScreen().findPreference("about_donate").setOnPreferenceClickListener(this);
        getPreferenceScreen().findPreference("about_open_source").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "about_Yuka":

                break;
            case "about_dev":

                break;
            case "about_donate":

                break;
            case "about_open_source":

                break;
        }
        return false;
    }
}
