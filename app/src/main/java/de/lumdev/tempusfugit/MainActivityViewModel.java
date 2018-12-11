package de.lumdev.tempusfugit;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.data.DataRepository;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;


public class MainActivityViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
//    private LiveData<List<GroupEvent>> allGroupEvents;
    public final LiveData<PagedList<GroupEvent>> allGroupEvents;
    private LiveData<List<de.lumdev.tempusfugit.data.Event>> allEvents;

    public MainActivityViewModel(Application application){
        super(application);
        dataRepository = new DataRepository(application);
//        allEvents = new LivePagedListBuilder<>(dataRepository.)
        allGroupEvents = dataRepository.getAllGroupEvents();
        allEvents = dataRepository.getAllEvents();
    }

//    LiveData<List<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };
    LiveData<PagedList<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };
    LiveData<List<Event>> getAllEvents(){ return allEvents; };
    public void insertGroupEvent(GroupEvent groupEvent){
        dataRepository.insertGroupEvent(groupEvent);
    }
    public void insertEvent(Event event){
        dataRepository.insertEvent(event);
    }

}
