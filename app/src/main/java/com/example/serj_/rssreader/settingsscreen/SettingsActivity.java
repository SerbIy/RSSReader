package com.example.serj_.rssreader.settingsscreen;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import com.example.serj_.rssreader.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setResult(R.integer.PREFERENCE_CHANGED);

    }

}
