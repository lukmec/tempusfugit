package de.lumdev.tempusfugit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //do here, whatever should be run after device boot

            //start my background notification service
            Log.d("TF_BOOT_RECIEVER", "Device Boot completed. Starting TF-Service.");
            context.startService(new Intent(context, PermanentNotificationService.class));

            //start or ensure, that CleanTaskListWorker is enqued by WorkManager
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */);
//            String cleanTasksEveryXHours = sharedPreferences.getString(context.getResources().getString(R.string.pref_id_time_clean_list), "24");
//            CleanTaskListWorker.enqueueSelf(context, Integer.valueOf((String) cleanTasksEveryXHours));
            //(alternative on-line implementation)
            CleanTaskListWorker.enqueueSelf(context, Integer.valueOf(
                    (String) PreferenceManager.getDefaultSharedPreferences(context)
                            .getString(context.getResources().getString(R.string.pref_id_time_clean_list), "-1")
            ));
        }
    }

}
