package de.lumdev.tempusfugit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.threeten.bp.Duration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import de.lumdev.tempusfugit.data.DataRepository;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;

public class MainViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private final LiveData<PagedList<GroupEvent>> allGroupEvents;
    private final LiveData<PagedList<Event>> allEvents;
    private final LiveData<PagedList<Event>> allNonArchivedEvents;
    private final LiveData<PagedList<GroupEvent>> allNonArchivedGroupEvents;

    public MainViewModel(Application application){
        super(application);
//        dataRepository = new DataRepository(application);
        dataRepository = DataRepository.getInstance(application);
        allGroupEvents = dataRepository.getAllGroupEvents();
        allEvents = dataRepository.getAllEvents();
        allNonArchivedEvents = dataRepository.getAllNonArchivedEvents();
        allNonArchivedGroupEvents = dataRepository.getAllNonArchivedGroupEvents();
    }

    //get List of all GroupEvents
    public LiveData<PagedList<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };

    //get List of all Events
    public LiveData<PagedList<Event>> getAllEvents(){ return allEvents; };

    //get List of all Visible Events
    public LiveData<PagedList<Event>> getAllNonArchivedEvents(){ return allNonArchivedEvents; };

    //get List of all Visible GroupEvents
    public LiveData<PagedList<GroupEvent>> getAllNonArchivedGroupEvents(){ return allNonArchivedGroupEvents; };

    //return either all archived (true) or all non-archived (false) groupEvents
    public LiveData<PagedList<GroupEvent>> getGroupEventsByArchiveState(boolean archived){
        return dataRepository.getGroupEventsByArchiveState(archived);
    }

    //return either all archived (true) or all non-archived (false) groupEvents
    public LiveData<PagedList<Event>> getEventsByArchiveState(boolean archived){
        return dataRepository.getEventsByArchiveState(archived);
    }

    //get List of all Events of specific GroupEvent
    public LiveData<PagedList<Event>> getAllEventsOfGroup(int groupEventId){
        return dataRepository.getAllEventsOfGroup(groupEventId, false);
    };

    //get List of all Events of specific toDoDay
    public LiveData<PagedList<Event>> getAllEventsOfToDoDay(int toDoDay){
        return dataRepository.getEventsOfToDoDay(toDoDay);
    };

//    private ArrayList<Integer> eventsWithNotificationToShow = new ArrayList<>();
//    private ArrayList<Integer> eventsWithDismissedNotification = new ArrayList<>();
//    public ArrayList<Integer> getEventIdsForNotificationForToDoDayZero(int maxNotificationsToShow){
//        if (eventsWithNotificationToShow.isEmpty()){
//            for (int i=0; i<dataRepository.getEventsOfToDoDay(0).getValue().size() && i<maxNotificationsToShow;i++){
//                int eventId = dataRepository.getEventsOfToDoDay(0).getValue().get(i).id;
//                if (!eventsWithDismissedNotification.contains(eventId)) {
//                    eventsWithNotificationToShow.add(eventId);
//                }
//            }
//        }
//        return eventsWithNotificationToShow;
//    }
//    public void setEventNotificationDismissed(int event_id){
//        eventsWithDismissedNotification.add(event_id);
//        eventsWithNotificationToShow.clear();
//    }

    //get single Group Event
    public LiveData<GroupEvent> getGroupEvent(int groupEventId){
        return dataRepository.getGroupEvent(groupEventId);
    }

    //get single Event
    public LiveData<Event> getEvent(int eventId){
        return dataRepository.getEvent(eventId);
    }

    //insert new Group Event to db
    public void insertGroupEvent(GroupEvent groupEvent){
        dataRepository.insertGroupEvent(groupEvent);
    }

    //update Group Event in db
    public void updateGroupEvent(GroupEvent groupEvent){
        dataRepository.updateGroupEvent(groupEvent);
    }

    //insert new Event to db
    public void insertEvent(Event event){
        dataRepository.insertEvent(event);
//        calculateToDoDateOfEvents();
    }

    //update Event in db
    public void updateEvent(Event event){
        dataRepository.updateEvent(event);
//        calculateToDoDateOfEvents();
    }

    //update archived state of event in db
    public void setEventArchivedState(int eventId, boolean archived){
        dataRepository.setEventArchivedState(eventId, archived);
    }

    //update archived state of groupEvent in db
    public void setGroupEventArchivedState(int groupEventId, boolean archived){
        dataRepository.setGroupEventArchivedState(groupEventId, archived);
    }

    //update done state of event in db
    public void setEventDone(int eventId, boolean done){
        dataRepository.setEventDone(eventId, done);
//        dataRepository.calculateGroupEventProgress(groupEventId); //---> not needed anymore, because Database Trigger handels calculation of progress
//        Log.d("-->", "ViewModel.setEventDone("+done+")");
    }

    //delete Event permanently in db
    public void deleteEvent(int eventId){
        dataRepository.deleteEventById(eventId);
    }

    //delete GroupEvent permanently in db
    public void deleteGroupEvent(int groupEventId, Context context){
        dataRepository.deleteGroupEventById(groupEventId);
        //check if, group that was deleted is set as default group in settings
        //if so, change default setting to "not set" (meaning no group selected; id = -1)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */);
//        String defaultGroupEventName = sharedPreferences.getString(context.getString(R.string.pref_id_default_group_event_name), context.getString(R.string.pref_default_group_event_none_selected));
        int defaultGroupEventId = sharedPreferences.getInt(context.getString(R.string.pref_id_default_group_event_id), -1);
        if (defaultGroupEventId == groupEventId){
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putInt(context.getString(R.string.pref_id_default_group_event_id), -1)
                    .putString(context.getString(R.string.pref_id_default_group_event_name), context.getString(R.string.pref_default_group_event_none_selected))
                    .apply();
        }
    }

    public void calculateToDoDateOfEvents(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */);
        String wrkCapaHrsStr = sharedPreferences.getString(context.getResources().getString(R.string.pref_id_working_capacity), "4");
//        String buffertimePercStr = sharedPreferences.getString(context.getResources().getString(R.string.pref_id_buffertime), "0");
        double buffertimePerc = sharedPreferences.getInt(context.getResources().getString(R.string.pref_id_buffertime), 0);
        double wrkCapaHrs = Double.valueOf(wrkCapaHrsStr);
//        int buffertimePerc = Integer.valueOf(buffertimePercStr);
        long totalCapaMinPerDay = Math.round((wrkCapaHrs*60)-(wrkCapaHrs * 60 * buffertimePerc / 100.0));
        Log.d("Total Wrk-Capa per Day", String.valueOf(totalCapaMinPerDay) + " minutes");

        Duration wrkCapaPerDay = Duration.ofMinutes(totalCapaMinPerDay);
        dataRepository.calculateToDoDateOfEvents(wrkCapaPerDay);

    }

    public void setDoneEventsArchived(){
        dataRepository.setDoneEventsArchived();
    }

    private String getEventsAsJson(){
        String json_string = "";
//        Gson gson = new Gson();
//        Event test_event = new Event("Test Event", "Das hat hier eine Beschreibung.", 1);
//        json_string = gson.toJson(test_event);
//        for (event : dataRepository.getAllEventsForBackup()) {
//            json_string = gson
//        }
//        try {
//            List<Event> list = dataRepository.getEventsForBackup();
//            json_string = gson.toJson(list.get(0));
////            json_string = gson.toJson(list);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.serializeNulls();
        try {
            List<Event> list = dataRepository.getEventsForBackup();
            json_string = builder.create().toJson(list);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        String event_string = "{ \"archived\": true, \"color\": -12707, \"creationDate\": { \"dateTime\": { \"date\": { \"day\": 1, \"month\": 8, \"year\": 2019 }, \"time\": { \"hour\": 21, \"minute\": 28, \"nano\": 693000000, \"second\": 53 } }, \"offset\": { \"totalSeconds\": 0 } }, \"description\": \" \", \"done\": true, \"doneDateTime\": null, \"dueDate\": null, \"duration\": { \"nanos\": 0, \"seconds\": 3600 }, \"icon\": 2131230845, \"id\": 1, \"importance\": 1.0, \"name\": \"Einleitung schreiben\", \"parentId\": 1, \"priority\": 2.0, \"textColor\": -16777216, \"toDoDay\": 0, \"urgency\": 1.0 }";
//        Event read_event = new Gson().fromJson(event_string, Event.class);
//        String event_string_2 = "[{ \"archived\": true, \"color\": -12707, \"creationDate\": { \"dateTime\": { \"date\": { \"day\": 1, \"month\": 8, \"year\": 2019 }, \"time\": { \"hour\": 21, \"minute\": 28, \"nano\": 693000000, \"second\": 53 } }, \"offset\": { \"totalSeconds\": 0 } }, \"description\": \" \", \"done\": true, \"doneDateTime\": null, \"dueDate\": null, \"duration\": { \"nanos\": 0, \"seconds\": 3600 }, \"icon\": 2131230845, \"id\": 1, \"importance\": 1.0, \"name\": \"Einleitung schreiben\", \"parentId\": 1, \"priority\": 2.0, \"textColor\": -16777216, \"toDoDay\": 0, \"urgency\": 1.0 },{ \"archived\": true, \"color\": -12707, \"creationDate\": { \"dateTime\": { \"date\": { \"day\": 1, \"month\": 8, \"year\": 2019 }, \"time\": { \"hour\": 21, \"minute\": 28, \"nano\": 693000000, \"second\": 53 } }, \"offset\": { \"totalSeconds\": 0 } }, \"description\": \" \", \"done\": true, \"doneDateTime\": null, \"dueDate\": null, \"duration\": { \"nanos\": 0, \"seconds\": 3600 }, \"icon\": 2131230845, \"id\": 1, \"importance\": 1.0, \"name\": \"Einleitung schreiben\", \"parentId\": 1, \"priority\": 2.0, \"textColor\": -16777216, \"toDoDay\": 0, \"urgency\": 1.0 }]";
//        Type listType = new TypeToken<List<Event>>(){}.getType();
//        List<Event> read_list = new Gson().fromJson(event_string, listType);
//        try {
//            List<Event> list = dataRepository.getEventsForBackup();
////            json_string = list.get(0).creationDate +"  ----  "+read_event.creationDate;
//            json_string = read_list.toString();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        dataRepository.insertEvent(read_event);
        return json_string;
    }

    private String getGroupEventsAsJson() {
        String json_string = "";
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.serializeNulls();
        try {
            List<GroupEvent> list = dataRepository.getGroupEventsForBackup();
            json_string = builder.create().toJson(list);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return json_string;
    }

    public String getDatabaseAsJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"events\":");
        builder.append(getEventsAsJson());
        builder.append(", \"group_events\":");
        builder.append(getGroupEventsAsJson());
        builder.append("}");

        return builder.toString();
    }
}
