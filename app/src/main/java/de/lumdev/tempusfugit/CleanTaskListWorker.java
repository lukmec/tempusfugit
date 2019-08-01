package de.lumdev.tempusfugit;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CleanTaskListWorker extends Worker {

    public static final String PERIODIC_WORK_REQUEST_CLEAN_TASK_LIST = "PERIODIC_WORK_REQUEST_CLEAN_TASK_LIST";

    public CleanTaskListWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    //static method for enqueing Worker itself
    //enqueUniquePeriodicWork in combination with Policy.REPLACE ensures, that worker is updated with new execution interval
    //(see https://stackoverflow.com/questions/53043183/how-to-register-a-periodic-work-request-with-workmanger-system-wide-once-i-e-a )
    // https://developer.android.com/reference/androidx/work/WorkManager.html#enqueueUniquePeriodicWork(java.lang.String,%20androidx.work.ExistingPeriodicWorkPolicy,%20androidx.work.PeriodicWorkRequest)
    public static void enqueueSelf(Context context, int cleanEveryXHours) {
        //enque Worker, unless "-1! is set in preferences, meaning worker should not be run
        if (cleanEveryXHours != -1) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(PERIODIC_WORK_REQUEST_CLEAN_TASK_LIST, ExistingPeriodicWorkPolicy.REPLACE,
                    new PeriodicWorkRequest.Builder(CleanTaskListWorker.class, cleanEveryXHours, TimeUnit.HOURS)
                            .build());
            Log.d("TF_CleanTasksWorker", "Clean Task List Worker enqueued by WorkManager.");
        }else{
            WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK_REQUEST_CLEAN_TASK_LIST);
            Log.d("TF_CleanTasksWorker", "Clean Task List Worker cancelled by WorkManager.");
        }
    }

    @Override
    public Result doWork() {
        Log.d("TF_CleanTasksWorker", "Clean Task List Worker started. All done Events are now set archived.");
        //set all done tasks to archived
        MainViewModel viewModel = new MainViewModel((Application) getApplicationContext());
        viewModel.setDoneEventsArchived();
        viewModel.calculateToDoDateOfEvents(getApplicationContext());

        //send broadcast for service to periodically clear list of events to ignore (hoping to avoid a endlessly growing arraylist)
            //when done events are archived by this worker, they dont show up on daily todolist, therefore will also be never again get a notif for them
        Intent intent = new Intent();
        intent.setAction(PermanentNotificationService.ACTION_CLEAR_EVENTS_TO_IGNORE_LIST);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        try{
            pendingIntent.send();
        }catch (PendingIntent.CanceledException e){
            Log.d("TF_Perso_Settings", "PendingIntent.CanceledException raised. Sending request to clear EventsToIgnoreList not successfull.");
        }
        //send broadcast for service to recreate all notifications (if worker set events to archived, service should refresh list of totoevents and decide freshly which events to notify)
        Intent intent2 = new Intent();
        intent2.setAction(PermanentNotificationService.ACTION_RECREATE_NOTIFICATIONS);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        try{
            pendingIntent2.send();
        }catch (PendingIntent.CanceledException e){
            Log.d("TF_Perso_Settings", "PendingIntent.CanceledException raised. Sending request to recreate event notifications not successfull.");
        }

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

}
