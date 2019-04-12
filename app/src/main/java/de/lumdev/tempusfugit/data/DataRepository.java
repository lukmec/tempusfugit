package de.lumdev.tempusfugit.data;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
//import de.lumdev.event_logic.BasicEvent;
//import de.lumdev.event_logic.Event;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class DataRepository {

    private GroupEventDao myGroupEventDao;
    private EventDao myEventDao;
    private LiveData<PagedList<GroupEvent>> allGroupEvents;
    private LiveData<PagedList<Event>> allEvents;
    private LiveData<PagedList<Event>> allNonArchivedEvents;

    private static DataRepository ourInstance;
    public static DataRepository getInstance(Application application) {
        if (ourInstance == null){
            ourInstance = new DataRepository(application);
        }
        return ourInstance;
    }

    public DataRepository(Application application){
        LocalRoomDatabase db = LocalRoomDatabase.getDatabase(application);
        myGroupEventDao = db.groupEventDao();
        myEventDao = db.eventDao();
//        allGroupEvents = myGroupEventDao.getAllGroupEvents(); //old implementation without factory
        allGroupEvents = new LivePagedListBuilder<>(myGroupEventDao.getAllGroupEvents(),20).build();
//        allEvents = myEventDao.getAllEvents(); //old implementation without factory
        allEvents = new LivePagedListBuilder<>(myEventDao.getAllEvents(),20).build();
        allNonArchivedEvents = new LivePagedListBuilder<>(myEventDao.getEventsByArchiveState(false),20).build();
    }

    public LiveData<PagedList<GroupEvent>> getAllGroupEvents(){
        return allGroupEvents;
    }
    public LiveData<PagedList<Event>> getAllEvents(){
        return allEvents;
    }
    public LiveData<PagedList<Event>> getAllNonArchivedEvents(){
        return allNonArchivedEvents;
    }
    // List<Event> getEventsInListByPriority() --> see below for implementation in Async Task
    public LiveData<PagedList<Event>> getAllEventsOfGroup(int groupEventId, boolean archived){
        return new LivePagedListBuilder<>(myEventDao.getEventsOfParent(groupEventId, archived),20).build();
    }
    public LiveData<PagedList<Event>> getEventsOfToDoDay(int toDoDay){
        return new LivePagedListBuilder<>(myEventDao.getEventsByToDoDay(toDoDay),20).build();
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
    public void setEventArchivedState(int eventId, boolean archived){
        new setEventArchivedStateAsyncTask(myEventDao, eventId, archived).execute();
    }
    private static class setEventArchivedStateAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mAsyncTaskDao;
        private int mEventId;
        private boolean mArchived;
        setEventArchivedStateAsyncTask(EventDao dao, int eventId, boolean archived) {
            mAsyncTaskDao = dao;
            mEventId = eventId;
            mArchived = archived;
        }
        @Override
        protected Void doInBackground(Void ... params) {
            mAsyncTaskDao.setArchiveState(mEventId, mArchived);
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

    //-------- calculate ToDoDay for Events (of non-archived events) ----------
//    public void calculateToDoDateOfEvents(Duration workingCapacityPerDay, List<Event> eventsByPriority){
//
//        int toDoDay = 0;
//        Duration remainingWorkingCapacity = Duration.ZERO;
//        remainingWorkingCapacity = remainingWorkingCapacity.plus(workingCapacityPerDay);
//
//        Log.d("--> RemWrkCapa (hrs)", String.valueOf(remainingWorkingCapacity.toHours()));
//
//        for (Event event : eventsByPriority) {
////                Log.d("--> Event.id", String.valueOf(event.id));
//            remainingWorkingCapacity = remainingWorkingCapacity.minus(event.duration);
////           Log.d("--> RemWrkCapa (min)", String.valueOf(remainingWorkingCapacity.toMinutes()));
//            if (remainingWorkingCapacity.isNegative()) {
//                toDoDay += 1;
//                remainingWorkingCapacity = Duration.ZERO;
//                remainingWorkingCapacity = remainingWorkingCapacity.plus(workingCapacityPerDay);
//            } else { //remaining Capacity is 0 or above (meaning task can still be fulfilled)
//                new setToDoDateOfEventsAsyncTask(myEventDao, event.id, toDoDay).execute();
//                Log.d("Event Nr. " + event.id, "Day: " + event.toDoDay + " --- Remaining Time on day: " + String.valueOf(remainingWorkingCapacity.toMinutes()));
//            }
//        }
//    }
//
//    private List<Event> getEventsInListByPriority() throws ExecutionException, InterruptedException {
//        return new getAllAsyncTask(myEventDao).execute().get();
//    }
//    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<Event>> {
//        private EventDao mEventAsyncTaskDao;
//        List<Event> a;
//        getAllAsyncTask(EventDao eventDao) {
//            mEventAsyncTaskDao = eventDao;
//        }
//        @Override
//        protected List<Event> doInBackground(Void... voids) {
//            return mEventAsyncTaskDao.getAllEventsInListByPriority();
//        }
//    }

    private static class setToDoDateOfEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao mEventAsyncTaskDao;
        private int mEventId;
        private int mToDoDay;
        setToDoDateOfEventsAsyncTask(EventDao eventDao, int eventId, int toDoDay) {
            mEventAsyncTaskDao = eventDao;
            mEventId = eventId;
            mToDoDay = toDoDay;
        }
        @Override
        protected Void doInBackground(Void ... params) {
            mEventAsyncTaskDao.setToDoDay(mEventId, mToDoDay);
            return null;
        }
    }

    public void calculateToDoDateOfEvents(Duration workingCapacityPerDay) {
        new CalculateToDoDateOfEventsAsyncTask(myEventDao, workingCapacityPerDay).execute();
    }
    private static class CalculateToDoDateOfEventsAsyncTask extends AsyncTask<Void, Void, List<Event>> {
        private EventDao mEventAsyncTaskDao;
        private Duration mWorkingCapacityPerDay;
        List<Event> a;
        CalculateToDoDateOfEventsAsyncTask(EventDao eventDao, Duration workingCapacityPerDay) {
            mEventAsyncTaskDao = eventDao;
            mWorkingCapacityPerDay = workingCapacityPerDay;
        }
        @Override
        protected List<Event> doInBackground(Void... voids) {
            return mEventAsyncTaskDao.getAllEventsInListByPriority();
        }
        @Override
        protected void onPostExecute(List<Event> eventsByPriority) {

            boolean ERR_WRKCAPA_NOT_SUFFICIENT_FOR_SINGLE_EVENT = false;
            Duration unusedTimeOnDayZero = Duration.ZERO;

            int toDoDay = 0;
            Duration remainingWorkingCapacity = Duration.ZERO;
            remainingWorkingCapacity = remainingWorkingCapacity.plus(mWorkingCapacityPerDay);

            Log.d("--> RemWrkCapa (hrs)", String.valueOf(remainingWorkingCapacity.toHours()));

            Event event = null;
            //set proper toDoDay to events
            for (int i = 0; i < eventsByPriority.size(); i++){
                event = eventsByPriority.get(i);
                remainingWorkingCapacity = remainingWorkingCapacity.minus(event.duration);
                if (remainingWorkingCapacity.isNegative()) {
                    if (toDoDay == 0) unusedTimeOnDayZero = unusedTimeOnDayZero.plus(remainingWorkingCapacity); //set value for displaying in UI
                    toDoDay += 1;
                    remainingWorkingCapacity = Duration.ZERO;
                    remainingWorkingCapacity = remainingWorkingCapacity.plus(mWorkingCapacityPerDay);
                    i -= 1; //redo iteration in order to set day to element (otherwise element will be skipped)
                    if (event.duration.compareTo(mWorkingCapacityPerDay) > 0){
                        Log.d("ERROR","Event takes longer than Working Capacity per Day is available.");
                                                i = eventsByPriority.size(); //causes loop to stop (and don't do further iterations)
                        ERR_WRKCAPA_NOT_SUFFICIENT_FOR_SINGLE_EVENT = true; //set flag to indicate error
                        if (ERR_WRKCAPA_NOT_SUFFICIENT_FOR_SINGLE_EVENT){
                            //set tododay of all tasks/ events to -1
                            Event brokenEvent = null;
                            for (int j = 0; j < eventsByPriority.size(); j++){
                                brokenEvent = eventsByPriority.get(j);
                                new setToDoDateOfEventsAsyncTask(mEventAsyncTaskDao, brokenEvent.id, -1).execute();
                            }
                        }
                        new setToDoDateOfEventsAsyncTask(mEventAsyncTaskDao, event.id, 0).execute(); //set event that failed to 0 (in order to make it visible in UI)
                    }
                } else {
                    //remaining Capacity is (now) 0 or above (meaning task can still be fulfilled --- toDoDay may be updated)
                    new setToDoDateOfEventsAsyncTask(mEventAsyncTaskDao, event.id, toDoDay).execute(); //set proper day to event
//                    Log.d("Event Nr. " + event.id, "Day: " + event.toDoDay +"("+toDoDay+")" + " --- Remaining Time on day: " + String.valueOf(remainingWorkingCapacity.toMinutes()));
                }
            }
        }
    }


}
