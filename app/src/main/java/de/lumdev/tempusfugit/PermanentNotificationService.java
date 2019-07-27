package de.lumdev.tempusfugit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;
import androidx.preference.PreferenceManager;
import de.lumdev.tempusfugit.data.Event;

    public class PermanentNotificationService extends LifecycleService {

        private int numberOfTasksWithPermanentNotification = 3;
        public final int PERMANENT_NOTIFICATION_QUICK_ACCESS = 100;
        private final String PERMANENT_NOTIFICATION_TASKS_CHANNEL = "1";
        private MainViewModel viewModel;
        private NotificationManagerCompat notificationManager;
        private ArrayList<NotificationCompat.Builder> allActiveNotifications = new ArrayList<>();
        private ArrayList<Integer> eventsToIgnore = new ArrayList<>();
        //static final vlaues for BroadcastReciever
        public static final String ACTION_EVENT_DONE_STATE_CHANGE = "Action_Event_Done_State_Change";
        public static final String ACTION_NOTIFICATION_DISMISSED = "Action_Notification_Dismissed";
        public static final String EXTRA_EVENT_ID = "Extra_Event_Id";
        public static final String EXTRA_EVENT_DONE = "Extra_Event_Done";
        private BroadcastReceiver notificationProcessingBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals(ACTION_EVENT_DONE_STATE_CHANGE)) {
                    // your onClick action is here
                    if (intent.hasExtra(EXTRA_EVENT_ID) && intent.hasExtra(EXTRA_EVENT_DONE)) {
                        viewModel.setEventDone(intent.getIntExtra(EXTRA_EVENT_ID, -1), intent.getBooleanExtra(EXTRA_EVENT_DONE, false));
                        //log info out
//                    Toast.makeText(context, "Event updated.", Toast.LENGTH_SHORT).show();
//                    Log.d("--->", "EventId:"+intent.getIntExtra(EXTRA_EVENT_ID, -1)+"      eventDone: "+intent.getBooleanExtra(EXTRA_EVENT_DONE, false));
                    }
                }
                if (intent.getAction().equals(ACTION_NOTIFICATION_DISMISSED)) {
                    // your onClick action is here
                    if (intent.hasExtra(EXTRA_EVENT_ID) && intent.hasExtra(EXTRA_EVENT_DONE)) {
                        //ignore event to show in notifications //prevent event, that was dismissed from notifiaction to be displayed again
                        eventsToIgnore.add(intent.getIntExtra(EXTRA_EVENT_ID, -1));
                        //ATTENTION: "CHEAT-MOVE" --- do random database operation (i.e. re-saving done-state) to trigger change in livData set (without this "triggering" no new event would be loaded)
                        viewModel.setEventDone(intent.getIntExtra(EXTRA_EVENT_ID, -1), intent.getBooleanExtra(EXTRA_EVENT_DONE, false));
                        //log info out
//                    Toast.makeText(context, "Event "+ intent.getIntExtra(EXTRA_EVENT_ID, -1) +" dismissed.", Toast.LENGTH_SHORT).show();
//                    Log.d("--->", "EventId:"+intent.getIntExtra(EXTRA_EVENT_ID, -1)+" dismissed.");
                    }
                }
            }
        };

    public PermanentNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TF_PermNotifService", "Service started.");

        //check whether service is wanted to start at all
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext() /* Activity context */);
//        Log.d("---->", "show notif from prefs: "+sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_id_show_notification), false));
        if (!sharedPreferences.getBoolean(getApplicationContext().getResources().getString(R.string.pref_id_show_notification), false)){
            stopSelf();
        }

        viewModel = new MainViewModel(getApplication());
        getApplicationContext().registerReceiver(notificationProcessingBroadcastReceiver, new IntentFilter(ACTION_EVENT_DONE_STATE_CHANGE)); //register BroadcastReciever to be able to respond to clicks on notification
        getApplicationContext().registerReceiver(notificationProcessingBroadcastReceiver, new IntentFilter(ACTION_NOTIFICATION_DISMISSED));
        createEventNotifications();

//        ArrayList<Integer> testEvents = viewModel.getEventIdsForNotificationForToDoDayZero(3);
//        for (int i=0; i<testEvents.size();i++){
//            Log.d("----->", "Notification planned for Event Nr. "+testEvents.get(i).toString());
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("TF_PermNotifService", "Service stopped.");

        //make all notifcations disappear
        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }
        notificationManager.cancelAll();

        //unregister Receiver for listening to onClickEvents (and dismiss actions) on notifcations
        getApplicationContext().unregisterReceiver(notificationProcessingBroadcastReceiver); //unregister BroadcastReciever to free memory
        super.onDestroy();
    }


    private void createEventNotifications(){

        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }

        //initalize arrylist of NotificationBuilders
        initListOfActiveNotifications();

        viewModel.getAllEventsOfToDoDay(0).observe(this, events -> {

            for (int i=0; i < events.size() && i < numberOfTasksWithPermanentNotification; i++) {
                Event e = events.get(i);

                if (!eventsToIgnore.contains(e.id)) { //ignore specific event (ignore == dont create notification), when in list for ignoredEvents

                    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_single_task_permanent);
                    if (!e.done) {
                        contentView.setImageViewResource(R.id.notif_image_front, R.drawable.ic_check_box_outline_blank_black_24dp);
                        contentView.setInt(R.id.notif_name, "setPaintFlags", 1281);
//                        contentView.setInt(R.id.notif_desc, "setPaintFlags", 1281);
                    } else {
                        contentView.setImageViewResource(R.id.notif_image_front, R.drawable.ic_check_box_black_24dp);
                        contentView.setInt(R.id.notif_name, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG);
//                        contentView.setInt(R.id.notif_desc, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    contentView.setTextViewText(R.id.notif_name, e.name);
//                    contentView.setTextViewText(R.id.notif_desc, e.description);
                    contentView.setOnClickPendingIntent(R.id.notif_single_task_relative_layout, getEventDonePendingIntent(getApplicationContext(), i, e.id, !e.done)); //!e.done equals the new desired Done State
                    contentView.setImageViewResource(R.id.notif_event_icon, e.icon);
                    //adapt colors
                    contentView.setTextColor(R.id.notif_name, e.textColor);
//                    contentView.setTextColor(R.id.notif_desc, e.textColor);
                    contentView.setInt(R.id.notif_single_task_relative_layout, "setBackgroundColor", e.color);
                    contentView.setInt(R.id.notif_image_front, "setColorFilter", e.textColor);
                    contentView.setInt(R.id.notif_event_icon, "setColorFilter", e.textColor);

                    NotificationCompat.Builder notificationBuilder = allActiveNotifications.get(i);
                    notificationBuilder
                            .setSmallIcon(e.icon)
//                            .setColor(getResources().getColor(R.color.groupEvent_green))
//                            .setSmallIcon(R.drawable.ic_star_black_24dp)
                            .setCustomContentView(contentView)
                            .setDeleteIntent(getNotificationDismissedPendingIntent(getApplicationContext(), i, e.id, !e.done))
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                            .setShowWhen(true)
//                            .setColorized(true)
//                            .setVisibility(NotificationCompat.VISIBILITY_SECRET)

                            .setOngoing(!e.done);
//                                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                                    .setCustomBigContentView(notificationLayoutExpanded)

                    notificationManager.notify(i, allActiveNotifications.get(i).build());
                }else{
                    //since one event is ignored (no notif for event with specific id shown), create a new "place" (meaning notifBuilder) for next event in ToDoList
                    allActiveNotifications.add(new NotificationCompat.Builder(getApplicationContext(), PERMANENT_NOTIFICATION_TASKS_CHANNEL));
                    numberOfTasksWithPermanentNotification++;
                }
            }
        });
    }

    public void createQuickAccessNotification(String testMsg){
        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), PERMANENT_NOTIFICATION_TASKS_CHANNEL);
        notificationBuilder.setContentTitle(testMsg)
                .setOngoing(true);
        notificationManager.notify(PERMANENT_NOTIFICATION_QUICK_ACCESS, notificationBuilder.build());
    }

    public void setNumberOfTasksWithPermanentNotification(int numberOfTaskToShow){
        numberOfTasksWithPermanentNotification = numberOfTaskToShow;
        allActiveNotifications.clear();
        initListOfActiveNotifications();
    }

    private void initListOfActiveNotifications(){
        for (int i=0; i < numberOfTasksWithPermanentNotification; i++){
            allActiveNotifications.add(i, new NotificationCompat.Builder(getApplicationContext(), PERMANENT_NOTIFICATION_TASKS_CHANNEL));
        }
    }

    public PendingIntent getEventDonePendingIntent(Context context,int notif_index, int eventId, boolean eventDone) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EVENT_ID, eventId); // Attention: Adding an extra always requires App to be re-installed!! (extra do't work properly otherwise)
        intent.putExtra(EXTRA_EVENT_DONE, eventDone);
        intent.setAction(ACTION_EVENT_DONE_STATE_CHANGE);
        return PendingIntent.getBroadcast(context, notif_index, intent, PendingIntent.FLAG_CANCEL_CURRENT); //Attention: Flag_Update_Current needed in order to update Extras of Pending intent, every time user clicks //FLAG_CANCEL_CURRENT for cancelling privious pIntent and creating a new one
        // read more here: https://developer.android.com/reference/android/app/PendingIntent.html#getBroadcast-android.content.Context-int-android.content.Intent-int-
    }

    public PendingIntent getNotificationDismissedPendingIntent(Context context,int notif_index, int eventId, boolean eventDone) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EVENT_ID, eventId); // Attention: Adding an extra always requires App to be re-installed!! (extra do't work properly otherwise)
        intent.putExtra(EXTRA_EVENT_DONE, eventDone);
        intent.setAction(ACTION_NOTIFICATION_DISMISSED);
        return PendingIntent.getBroadcast(context, notif_index, intent, PendingIntent.FLAG_CANCEL_CURRENT); //Attention: Flag_Update_Current needed in order to update Extras of Pending intent, every time user clicks //FLAG_CANCEL_CURRENT for cancelling privious pIntent and creating a new one
        // read more here: https://developer.android.com/reference/android/app/PendingIntent.html#getBroadcast-android.content.Context-int-android.content.Intent-int-
    }
}
