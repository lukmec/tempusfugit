package de.lumdev.tempusfugit.settings;

import android.os.Bundle;

import de.lumdev.tempusfugit.R;

public class SettingsPersonalizationFragment extends SettingsTemplateFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_personalization, rootKey);
        super.setupToolbar(R.string.pref_page_personalization);
    }

}
