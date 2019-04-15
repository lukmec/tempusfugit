package de.lumdev.tempusfugit.settings;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.SeekBarPreference;
import de.lumdev.tempusfugit.R;

public class SettingsPlanningPrefsFragment extends SettingsTemplateFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_planning_prefs, rootKey);
        super.setupToolbar(R.string.pref_page_planning_preferences);

        //dynamically set summary for seekbar
        SeekBarPreference buffertimePreference = (SeekBarPreference) findPreference("buffertime");
        buffertimePreference.setSummary(buffertimePreference.getValue()+"%");
//        buffertimePreference.setShowSeekBarValue(true);
        buffertimePreference.setUpdatesContinuously(true);
//        buffertimePreference.setSummaryProvider(new Preference.SummaryProvider<SeekBarPreference>() {
//            @Override
//            public CharSequence provideSummary(SeekBarPreference preference) {
//                return preference.getValue() + "%";
//            }
//        });
        //set listener for updating summary, when user changes value
        buffertimePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString() + "%");
                return true; //return false, if newValue should not be saved; true, if newValue should be set and saved
            }
        });


    }

}
