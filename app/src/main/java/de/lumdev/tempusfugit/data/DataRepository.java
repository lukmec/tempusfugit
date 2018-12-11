package de.lumdev.tempusfugit.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
//import de.lumdev.event_logic.BasicEvent;
//import de.lumdev.event_logic.Event;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.data.EventDao;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.GroupEventDao;
import de.lumdev.tempusfugit.data.LocalRoomDatabase;

public class DataRepository {

    private GroupEventDao myGroupEventDao;
    private EventDao myEventDao;
    private LiveData<PagedList<GroupEvent>> allGroupEvents;
    private LiveData<List<Event>> allEvents;

    public DataRepository(Application application){
        LocalRoomDatabase db = LocalRoomDatabase.getDatabase(application);
        myGroupEventDao = db.groupEventDao();
        myEventDao = db.eventDao();
//        allGroupEvents = myGroupEventDao.getAllGroupEvents();
        allGroupEvents = new LivePagedListBuilder<>(myGroupEventDao.getAllGroupEvents(),
                /*page size*/ 20).build();
        allEvents = myEventDao.getAllEvents();
    }

    public LiveData<PagedList<GroupEvent>> getAllGroupEvents(){
        return allGroupEvents;
    }
    public LiveData<List<Event>> getAllEvents(){
        return allEvents;
    }

    public void insertGroupEvent(GroupEvent groupEvent){
        new insertGroupEventAsyncTask(myGroupEventDao).execute(groupEvent);
    }
    private static class insertGroupEventAsyncTask extends AsyncTask<GroupEvent, Void, Void> {
        private GroupEventDao mAsyncTaskDao;
        insertGroupEventAsyncTask(GroupEventDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final GroupEvent... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insertEvent(Event event){
        new insertEventAsyncTask(myEventDao).execute(event);
    }
    private static class insertEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;
        insertEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public GroupEventDao getMyGroupEventDao() {
        return myGroupEventDao;
    }
}
