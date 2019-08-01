package de.lumdev.tempusfugit;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.widget.RemoteViews;

import com.maltaisn.icondialog.IconHelper;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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
        public static final String ACTION_SET_NUMBER_OF_NOTIFICATIONS_TO_SHOW = "ACTION_SET_NUMBER_OF_NOTIFICATIONS_TO_SHOW";
        public static final String ACTION_CLEAR_EVENTS_TO_IGNORE_LIST = "ACTION_CLEAR_EVENTS_TO_IGNORE_LIST";
        public static final String ACTION_RECREATE_NOTIFICATIONS = "ACTION_RECREATE_NOTIFICATIONS";
        public static final String EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW = "EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW";
        private BroadcastReceiver miscellaneousDataBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals(ACTION_SET_NUMBER_OF_NOTIFICATIONS_TO_SHOW)) {
                    if (intent.hasExtra(EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW)) {
//                        Log.d("-->>", "Broadcast reviced: "+intent.getIntExtra(EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW, 3));
                        setNumberOfTasksWithPermanentNotification(intent.getIntExtra(EXTRA_NUMBER_OF_NOTIFICATIONS_TO_SHOW, 3));
                        createEventNotifications();
                    }
                }
                if (intent.getAction().equals(ACTION_CLEAR_EVENTS_TO_IGNORE_LIST)) {
                    eventsToIgnore.clear();
                    Log.d("TF_PermNotifService", "List of EventsToIgnore cleared successfully (in order to avoid endless growing of list).");
                }
                if (intent.getAction().equals(ACTION_RECREATE_NOTIFICATIONS)) {
                    createEventNotifications();
                    Log.d("TF_PermNotifService", "Service is requested by broadcast to recreate all notifications.");
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

        //set inital number of notifications to show
        setNumberOfTasksWithPermanentNotification(Integer.valueOf(sharedPreferences.getString(getApplicationContext().getResources().getString(R.string.pref_id_number_of_notifications), "2")));

        viewModel = new MainViewModel(getApplication());
        getApplicationContext().registerReceiver(notificationProcessingBroadcastReceiver, new IntentFilter(ACTION_EVENT_DONE_STATE_CHANGE)); //register BroadcastReciever to be able to respond to clicks on notification
        getApplicationContext().registerReceiver(notificationProcessingBroadcastReceiver, new IntentFilter(ACTION_NOTIFICATION_DISMISSED));
        getApplicationContext().registerReceiver(miscellaneousDataBroadcastReceiver, new IntentFilter(ACTION_SET_NUMBER_OF_NOTIFICATIONS_TO_SHOW));
        getApplicationContext().registerReceiver(miscellaneousDataBroadcastReceiver, new IntentFilter(ACTION_CLEAR_EVENTS_TO_IGNORE_LIST));
        getApplicationContext().registerReceiver(miscellaneousDataBroadcastReceiver, new IntentFilter(ACTION_RECREATE_NOTIFICATIONS));
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

        cancelEventNotifications();

        //unregister Receiver for listening to onClickEvents (and dismiss actions) on notifcations
        getApplicationContext().unregisterReceiver(notificationProcessingBroadcastReceiver); //unregister BroadcastReciever to free memory
        getApplicationContext().unregisterReceiver(miscellaneousDataBroadcastReceiver);
        super.onDestroy();
    }


    private void createEventNotifications(){

        cancelEventNotifications(); //start fresh

        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }

        //initalize arrylist of NotificationBuilders
        initListOfActiveNotifications();

        viewModel.getAllEventsOfToDoDay(0).observe(this, events -> {

            int counter_current_number_of_notifs = 0;
//            for (int i=0; i < events.size() && i < numberOfTasksWithPermanentNotification; i++) {
            for (int i=0; i < events.size() && counter_current_number_of_notifs < numberOfTasksWithPermanentNotification; i++) {
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
                    contentView.setOnClickPendingIntent(R.id.notif_single_task_relative_layout, getEventDonePendingIntent(getApplicationContext(), counter_current_number_of_notifs, e.id, !e.done)); //!e.done equals the new desired Done State
                    //set icon of group event to notification
                    try{
                        //setting picture from icondialog id
//                        Drawable drawable = IconHelper.getInstance(getApplicationContext()).getIcon(e.icon).getDrawable(getApplicationContext());
                        contentView.setImageViewBitmap(R.id.notif_event_icon, drawableToBitmap(IconHelper.getInstance(getApplicationContext()).getIcon(e.icon).getDrawable(getApplicationContext())));
                    }catch (Exception exception){
                        //setting picture from R's resource id
                        contentView.setImageViewResource(R.id.notif_event_icon, e.icon);
                    }
                    //adapt colors
                    contentView.setTextColor(R.id.notif_name, e.textColor);
//                    contentView.setTextColor(R.id.notif_desc, e.textColor);
                    contentView.setInt(R.id.notif_single_task_relative_layout, "setBackgroundColor", e.color);
                    contentView.setInt(R.id.notif_image_front, "setColorFilter", e.textColor);
                    contentView.setInt(R.id.notif_event_icon, "setColorFilter", e.textColor);

                    NotificationCompat.Builder notificationBuilder = allActiveNotifications.get(counter_current_number_of_notifs);
                    notificationBuilder
//                            .setSmallIcon(e.icon)
//                            .setColor(getResources().getColor(R.color.groupEvent_green))
                            .setSmallIcon(R.drawable.ic_check_circle_white_24dp)
                            .setCustomContentView(contentView)
                            .setDeleteIntent(getNotificationDismissedPendingIntent(getApplicationContext(), counter_current_number_of_notifs, e.id, e.done))
                            .setPriority(Notification.PRIORITY_DEFAULT)
//                            .setPriority(NotificationCompat.PRIORITY_MIN)
                            .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                            .setShowWhen(true)
//                            .setColorized(true)
//                            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
//                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                            .setOngoing(!e.done);
//                                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                                    .setCustomBigContentView(notificationLayoutExpanded)

                    notificationManager.notify(i, allActiveNotifications.get(counter_current_number_of_notifs).build()); //important to notify with i (== index of event in list) in order to prevent, that same event-notification is shown multiple times
                    //increase number of currently shown notifs
                    counter_current_number_of_notifs++;
                } else {
//                    //since one event is ignored (no notif for event with specific id shown), create a new "place" (meaning notifBuilder) for next event in ToDoList
//                    allActiveNotifications.add(new NotificationCompat.Builder(getApplicationContext(), PERMANENT_NOTIFICATION_TASKS_CHANNEL));
//                        numberOfTasksWithPermanentNotification++;
                }
            }
        });
    }

    private void cancelEventNotifications(){
        //make all notifcations disappear
        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }
        notificationManager.cancelAll();
        eventsToIgnore.clear();
    }

    private void createQuickAccessNotification(String testMsg){
        if(notificationManager == null){
            notificationManager = NotificationManagerCompat.from(getApplicationContext());
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), PERMANENT_NOTIFICATION_TASKS_CHANNEL);
        notificationBuilder.setContentTitle(testMsg)
                .setOngoing(true);
        notificationManager.notify(PERMANENT_NOTIFICATION_QUICK_ACCESS, notificationBuilder.build());
    }

    private void setNumberOfTasksWithPermanentNotification(int numberOfTaskToShow){
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

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
