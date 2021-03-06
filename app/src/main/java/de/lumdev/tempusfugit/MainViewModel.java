package de.lumdev.tempusfugit;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.data.DataRepository;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.QuickDirtyDataHolder;
import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

public class MainViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private final LiveData<PagedList<GroupEvent>> allGroupEvents;
    private final LiveData<PagedList<Event>> allEvents;
    private final LiveData<PagedList<Event>> allVisibleEvents;

    public MainViewModel(Application application){
        super(application);
//        dataRepository = new DataRepository(application);
        dataRepository = DataRepository.getInstance(application);
        allGroupEvents = dataRepository.getAllGroupEvents();
        allEvents = dataRepository.getAllEvents();
        allVisibleEvents = dataRepository.getAllVisibleEvents();
    }

    //get List of all GroupEvents
    LiveData<PagedList<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };

    //get List of all Events
    LiveData<PagedList<Event>> getAllEvents(){ return allEvents; };

    //get List of all Visible Events
    LiveData<PagedList<Event>> getAllVisibleEvents(){ return allVisibleEvents; };

    //get List of all Events of specific GroupEvent
    LiveData<PagedList<Event>> getAllEventsOfGroup(int groupEventId){
        return dataRepository.getAllEventsOfGroup(groupEventId, true);
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
    }

    //update Event in db
    void updateEvent(Event event){
        dataRepository.updateEvent(event);
    }

    //update visibility of event in db
    void setEventVisibility(int eventId, boolean visible){
        dataRepository.setEventVisibility(eventId, visible);
    }

    //update done state of event in db
    void setEventDone(int eventId, int groupEventId, boolean done){
        dataRepository.setEventDone(eventId, done);
//        dataRepository.calculateGroupEventProgress(groupEventId); //---> not needed anymore, because Database Trigger handels calculation of progress
//        Log.d("-->", "ViewModel.setEventDone("+done+")");
    }



}
