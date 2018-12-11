package de.lumdev.tempusfugit;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.data.DataRepository;
import de.lumdev.tempusfugit.data.GroupEvent;

public class NewGroupEventViewModel extends AndroidViewModel {


    private DataRepository dataRepository;
    public final LiveData<PagedList<GroupEvent>> allGroupEvents;

    public NewGroupEventViewModel(Application application){
        super(application);
        dataRepository = new DataRepository(application);
        allGroupEvents = dataRepository.getAllGroupEvents();
    }

    public void insertGroupEvent(GroupEvent groupEvent){
        dataRepository.insertGroupEvent(groupEvent);
    }

}
