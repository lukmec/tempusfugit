package de.lumdev.tempusfugit.settings;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
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

        //set title string for toolbar (toolbar ist setup by SettingsTemplate via onActivityCreated())
        super.toolbarTitleResId = R.string.toolbar_settings_title;
        //        super.setupToolbar(R.string.toolbar_settings_title);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        super.setupToolbar(R.string.toolbar_settings_title);
//    }
}
