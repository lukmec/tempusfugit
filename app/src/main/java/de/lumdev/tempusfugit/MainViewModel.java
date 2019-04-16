package de.lumdev.tempusfugit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.threeten.bp.Duration;

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

    public MainViewModel(Application application){
        super(application);
//        dataRepository = new DataRepository(application);
        dataRepository = DataRepository.getInstance(application);
        allGroupEvents = dataRepository.getAllGroupEvents();
        allEvents = dataRepository.getAllEvents();
        allNonArchivedEvents = dataRepository.getAllNonArchivedEvents();
    }

    //get List of all GroupEvents
    LiveData<PagedList<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };

    //get List of all Events
    LiveData<PagedList<Event>> getAllEvents(){ return allEvents; };

    //get List of all Visible Events
    LiveData<PagedList<Event>> getAllNonArchivedEvents(){ return allNonArchivedEvents; };

    //get List of all Events of specific GroupEvent
    LiveData<PagedList<Event>> getAllEventsOfGroup(int groupEventId){
        return dataRepository.getAllEventsOfGroup(groupEventId, false);
    };

    //get List of all Events of specific toDoDay
    LiveData<PagedList<Event>> getAllEventsOfToDoDay(int toDoDay){
        return dataRepository.getEventsOfToDoDay(toDoDay);
    };

    //get single Group Event
    LiveData<GroupEvent> getGroupEvent(int groupEventId){
        return dataRepository.getGroupEvent(groupEventId);
    }

    //get single Event
    LiveData<Event> getEvent(int eventId){
        return dataRepository.getEvent(eventId);
    }

    //insert new Group Event to db
    void insertGroupEvent(GroupEvent groupEvent){
        dataRepository.insertGroupEvent(groupEvent);
    }

    //update Group Event in db
    void updateGroupEvent(GroupEvent groupEvent){
        dataRepository.updateGroupEvent(groupEvent);
    }

    //insert new Event to db
    void insertEvent(Event event){
        dataRepository.insertEvent(event);
//        calculateToDoDateOfEvents();
    }

    //update Event in db
    void updateEvent(Event event){
        dataRepository.updateEvent(event);
//        calculateToDoDateOfEvents();
    }

    //update visibility of event in db
    void setEventVisibility(int eventId, boolean visible){
        dataRepository.setEventArchivedState(eventId, visible);
    }

    //update done state of event in db
    void setEventDone(int eventId, int groupEventId, boolean done){
        dataRepository.setEventDone(eventId, done);
//        dataRepository.calculateGroupEventProgress(groupEventId); //---> not needed anymore, because Database Trigger handels calculation of progress
//        Log.d("-->", "ViewModel.setEventDone("+done+")");
    }

    void calculateToDoDateOfEvents(Context context){
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

}
