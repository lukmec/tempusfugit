package de.lumdev.tempusfugit.settings;


import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import de.lumdev.tempusfugit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsMainFragment extends SettingsTemplateFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);
        super.setupToolbar(R.string.toolbar_settings_title);
    }
}
