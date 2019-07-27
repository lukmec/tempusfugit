package de.lumdev.tempusfugit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import de.lumdev.tempusfugit.data.DataRepository;

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

//        DataRepository dataRepo = DataRepository.getInstance((Application) getApplicationContext());
//        dataRepo.setDoneEventsArchived();

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

}
