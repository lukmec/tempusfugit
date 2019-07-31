package de.lumdev.tempusfugit.settings;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
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

        //Handling setting number of Notifications
        ListPreference numberNotificationsList = findPreference(getString(R.string.pref_id_number_of_notifications));
        if (numberNotificationsList != null){
            numberNotificationsList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    numberNotificationsList.setValue((String) newValue);
                    //send broadcast for service to receive number of notifications to show
                    Intent intent = new Intent();
                    intent.putExtra(PermanentNotificationService.EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW, Integer.valueOf((String)newValue)); // Attention: Adding an extra always requires App to be re-installed!! (extra do't work properly otherwise)
                    intent.setAction(PermanentNotificationService.ACTION_SET_NUMBER_OF_NOTIFICATIONS_TO_SHOW);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    try{
                        pendingIntent.send();
                    }catch (PendingIntent.CanceledException e){
                        Log.d("TF_Perso_Settings", "PendingIntent.CanceledException raised. Sending new number of notifications to show not successfull.");
                    }
                    return false;
                }
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



