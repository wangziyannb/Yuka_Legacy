package com.wzy.yuka;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            settingFragment setting = new settingFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, setting)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }

    public static class settingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.setting);
            getPreferenceScreen().findPreference("about_Yuka").setOnPreferenceClickListener(this);
            getPreferenceScreen().findPreference("about_dev").setOnPreferenceClickListener(this);
            getPreferenceScreen().findPreference("about_donate").setOnPreferenceClickListener(this);
            getPreferenceScreen().findPreference("about_open_source").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()){
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
}
