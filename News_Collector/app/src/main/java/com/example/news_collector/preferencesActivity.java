package com.example.news_collector;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.prefs.Preferences;

public class preferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setOnPreferenceChangeListener(findPreference("key_push_time"));
        setOnPreferenceChangeListener(findPreference("key_alarm_sound"));

    }
//    푸시 알람 사용    : key_push_alarm
//    푸시 알람 설정    : key_push_time
//    알람 소리         : key_alarm_sound
//    저녁 알람 허용    : key_night_alarm
    // Setting Prefernces Summary
    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if(preference instanceof EditTextPreference){
                preference.setSummary(stringValue);
            }else if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;

                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }else if(preference instanceof RingtonePreference){
                if (TextUtils.isEmpty(stringValue)){
                    preference.setSummary("무음");

                }else{
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null){
                        preference.setSummary(null);
                    } else{
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            }
            return true;
        }
    };

    private void setOnPreferenceChangeListener(Preference mPreference){
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        onPreferenceChangeListener.onPreferenceChange(mPreference,
                PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }
}

