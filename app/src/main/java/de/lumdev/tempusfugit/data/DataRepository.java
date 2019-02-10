package de.lumdev.tempusfugit.data;

import android.app.Application;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
//import de.lumdev.event_logic.BasicEvent;
//import de.lumdev.event_logic.Event;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import de.lumdev.tempusfugit.R;
import de.lumdev.tempusfugit.data.EventDao;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.GroupEventDao;
import de.lumdev.tempusfugit.data.LocalRoomDatabase;
import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

public class DataRepository {

    private GroupEventDao myGroupEventDao;
    private EventDao myEventDao;
    private LiveData<PagedList<GroupEvent>> allGroupEvents;
    private LiveData<PagedList<Event>> allEvents;
    private LiveData<PagedList<Event>> allVisibleEvents;

    public DataRepository(Application application){
        LocalRoomDatabase db = LocalRoomDatabase.getDatabase(application);
        myGroupEventDao = db.groupEventDao();
        myEventDao = db.eventDao();
//        allGroupEvents = myGroupEventDao.getAllGroupEvents(); //old implementation without factory
        allGroupEvents = new LivePagedListBuilder<>(myGroupEventDao.getAllGroupEvents(),20).build();
//        allEvents = myEventDao.getAllEvents(); //old implementation without factory
        allEvents = new LivePagedListBuilder<>(myEventDao.getAllEvents(),20).build();
        allVisibleEvents = new LivePagedListBuilder<>(myEventDao.getVisibleEvents(),20).build();
    }

    public LiveData<PagedList<GroupEvent>> getAllGroupEvents(){
        return allGroupEvents;
    }
    public LiveData<PagedList<Event>> getAllEvents(){
        return allEvents;
    }
    public LiveData<PagedList<Event>> getAllVisibleEvents(){
        return allVisibleEvents;
    }
    public LiveData<PagedList<Event>> getAllEventsOfGroup(int groupEventId, boolean only_visible_events){
        return new LivePagedListBuilder<>(myEventDao.getEventsOfParent(groupEventId, only_visible_events),20).build();
    }

    /**
     * Get GroupEvent of specified ID
     * @param groupEventId
     * @return GroupEvent of specified ID or null
     */
    public LiveData<GroupEvent> getGroupEvent(int groupEventId){
        return myGroupEventDao.getGroupEvent(groupEventId);
    }

    /**
     * Get GroupEvent of specified ID
     * @param eventId
     * @return GroupEvent of specified ID or null
     */
    public LiveData<Event> getEvent(int eventId){
        return myEventDao.getEvent(eventId);
    }

    //--------insert new GroupEvent--------
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

    //--------update GroupEvent--------
    public void updateGroupEvent(GroupEvent groupEvent){
        new updateGroupEventAsyncTask(myGroupEventDao).execute(groupEvent);
    }
    private static class updateGroupEventAsyncTask extends AsyncTask<GroupEvent, Void, Void> {
        private GroupEventDao mAsyncTaskDao;
        updateGroupEventAsyncTask(GroupEventDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final GroupEvent... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    //--------insert new Event--------
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
//            SystemClock.sleep(1000);
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //--------update Event--------
    public void updateEvent(Event event){
        new updateEventAsyncTask(myEventDao).execute(event);
    }
    private static class updateEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao mAsyncTaskDao;
        updateEventAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    //--------update Event Visibility--------
    public void setEventVisibility(int eventId, boolean visible){
        new setEventVisibilityAsyncTask(myEventDao, eventId, visible).execute();
    }
    private static class setEventVisibilityAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mAsyncTaskDao;
        private int mEventId;
        private boolean mVisible;
        setEventVisibilityAsyncTask(EventDao dao, int eventId, boolean visible) {
            mAsyncTaskDao = dao;
            mEventId = eventId;
            mVisible = visible;
        }
        @Override
        protected Void doInBackground(Void ... params) {
            mAsyncTaskDao.setVisibility(mEventId, mVisible);
            return null;
        }
    }

    //--------update Event Done State--------
    public void setEventDone(int eventId, boolean done){
        new setEventDoneAsyncTask(myEventDao, eventId, done).execute();
//        Event event = new Event("test event", "with some description", 1);
//        Log.d("-->", "Event Created.");
//        new insertEventAsyncTask(myEventDao).execute(event);
    }
    private static class setEventDoneAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mEventAsyncTaskDao;
        private int mEventId;
        private boolean mDone;
        setEventDoneAsyncTask(EventDao eventDao, int eventId, boolean done) {
            mEventAsyncTaskDao = eventDao;
            mEventId = eventId;
            mDone = done;
        }
        @Override
        protected Void doInBackground(Void ... params) {
            OffsetDateTime doneDateTime = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
            mEventAsyncTaskDao.setDone(mEventId, mDone, doneDateTime);
            return null;
        }
    }

    //--------calculate GroupEvent Progress--------
    public void calculateGroupEventProgress(int groupEventId){
        new CalculateGroupEventProgressAsyncTask(myGroupEventDao, groupEventId).execute();
    }
    private static class CalculateGroupEventProgressAsyncTask extends AsyncTask<Void, Void, Void> {
        private GroupEventDao mAsyncTaskDao;
        private int mGroupEventId;
        private boolean mVisible;
        CalculateGroupEventProgressAsyncTask(GroupEventDao dao, int groupEventId) {
            mAsyncTaskDao = dao;
            mGroupEventId = groupEventId;
        }
        @Override
        protected Void doInBackground(Void ... params) {
            mAsyncTaskDao.calcProgress(mGroupEventId);
            return null;
        }
    }


}
