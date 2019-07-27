package de.lumdev.tempusfugit.settings;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import de.lumdev.tempusfugit.CleanTaskListWorker;
import de.lumdev.tempusfugit.MainViewModel;
import de.lumdev.tempusfugit.PermanentNotificationService;
import de.lumdev.tempusfugit.R;
import de.lumdev.tempusfugit.data.Event;

public class SettingsPersonalizationFragment extends SettingsTemplateFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_personalization, rootKey);

        //Handling showing permanent notification
        SwitchPreferenceCompat permNotifPref = findPreference(getString(R.string.pref_id_show_notification));
        if (permNotifPref != null){
            permNotifPref.setOnPreferenceClickListener( pref -> {
//                Log.d("TempusFugit_PersonalizationPreferences", "Permanent Notifications enabled: "+permNotifPref.isChecked());
                //define what to do, when switch is checked
                if (permNotifPref.isChecked()){
                    getActivity().startService(new Intent(getActivity(), PermanentNotificationService.class));
                }else{ //define what to do, when switch is not checked
                    getActivity().stopService(new Intent(getActivity(), PermanentNotificationService.class));
                }

                return true;
            });
        }
        //Handling Clearing of Task List
        ListPreference timeCleanList = findPreference(getString(R.string.pref_id_time_clean_list));
        if (timeCleanList != null){
            timeCleanList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    Log.d("Time CLean List oldValue", ""+timeCleanList.getValue());
//                    Log.d("Time CLean List newValue", ""+newValue);
                    timeCleanList.setValue((String) newValue);
                    CleanTaskListWorker.enqueueSelf(getContext(), Integer.valueOf((String) newValue));
                    return false;
                }
            });
        }

        super.setupToolbar(R.string.pref_page_personalization);
    }

}



