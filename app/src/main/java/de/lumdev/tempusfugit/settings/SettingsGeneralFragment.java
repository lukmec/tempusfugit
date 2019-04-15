package de.lumdev.tempusfugit.settings;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import de.lumdev.tempusfugit.R;

public class SettingsGeneralFragment extends SettingsTemplateFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);
        super.setupToolbar(R.string.pref_page_general);
    }

}
