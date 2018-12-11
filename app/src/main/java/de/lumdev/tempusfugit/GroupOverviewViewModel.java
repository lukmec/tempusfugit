package de.lumdev.tempusfugit;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.data.DataRepository;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;

public class GroupOverviewViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    public final LiveData<PagedList<GroupEvent>> allGroupEvents;

    public GroupOverviewViewModel(Application application){
        super(application);
        dataRepository = new DataRepository(application);
        allGroupEvents = dataRepository.getAllGroupEvents();
    }

    LiveData<PagedList<GroupEvent>> getAllGroupEvents(){ return allGroupEvents; };

}
