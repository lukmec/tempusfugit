package de.lumdev.tempusfugit.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import de.lumdev.tempusfugit.PermanentNotificationService;
import de.lumdev.tempusfugit.R;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.QuickDirtyDataHolder;
import de.lumdev.tempusfugit.fragments.SelectGroupEventDialogFragment;
import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

public class SettingsGeneralFragment extends SettingsTemplateFragment {

    private Context parentFragmentContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        //set title string for toolbar (toolbar ist setup by SettingsTemplate via onActivityCreated())
        super.toolbarTitleResId = R.string.pref_page_general;

        parentFragmentContext = getContext();

        //Handling setting of default GroupEvent
        Preference defaultGEPref = findPreference(getString(R.string.pref_id_default_group_event));
        if (defaultGEPref != null){

            //set last saved value in sharedPreferences as a summary (for user to know, if anything is already selected)
            defaultGEPref.setSummary(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_id_default_group_event_name), getString(R.string.pref_default_group_event_none_selected)));

            defaultGEPref.setOnPreferenceClickListener( pref -> {
//                Log.d("TempusFugit_GeneralPreferences", "User clicked on selecting default Group Preference.");
                //define what to do, when user clicked on preference
                SelectGroupEventDialogFragment selectGroupEventFragment = new SelectGroupEventDialogFragment();

                QuickDirtyDataHolder.getInstance().addSelectGEDialogObserver(new SelectGEDialogObserver() {
                    //observer which helps detecting on which item (groupevent) the user clicked (in SelectGroupEventDialog)
                    @Override
                    public void onClickGroupEvent(GroupEvent groupEvent) {
                        Log.d("--->","You clicked on GroupEvent with ID: "+groupEvent.id);
                        selectGroupEventFragment.dismiss();
                        //update value of Preference
                        defaultGEPref.setSummary(groupEvent.name);

                      //save values in shared preferences //------ if done here, app always crashes, because context is not availbale --> hence outsourced to private method and it works
//                        getActivity().getPreferences(Context.MODE_PRIVATE).edit()
//                        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
//                        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit()
//                                .putInt(getString(R.string.pref_id_default_group_event_id), groupEvent.id)
//                                .putString(getString(R.string.pref_id_default_group_event_name), groupEvent.name)
//                                .apply();
                        if (isAdded()) {
                            saveToSharedPreferences(groupEvent);
                        }else{
                            Log.d("TF_SettGenFragm", "Fragment is detached from context/ activity. (This displays instead of calling getContext(), which would crash the app.)");
                            //onClick in selectionDialog is called for whatever reason multiple times and result is getting back at different times.
                            //It happens quite easily, that getContext() is for some results not available anymore and the app crashes.
                        }
                    }
                });

                selectGroupEventFragment.show(getFragmentManager(), "selectGroupEventDialog");

                return true;
            });
        }

    }

    private void saveToSharedPreferences(GroupEvent groupEvent){
        //save values from a selected groupEvent (if one selected) to Shared Preferences
        if(groupEvent != null){
//            Log.d("--->", "saveToSharedPrefs() called.");
            PreferenceManager.getDefaultSharedPreferences(parentFragmentContext).edit()
                            .putInt(getString(R.string.pref_id_default_group_event_id), groupEvent.id)
                            .putString(getString(R.string.pref_id_default_group_event_name), groupEvent.name)
                            .apply();
            Log.d("TF_SettGenFragm", "DefaultGroupEvent saved successfully in SharedPreferences.");
        }
    }

}
