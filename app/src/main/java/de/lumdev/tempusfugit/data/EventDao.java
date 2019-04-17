package de.lumdev.tempusfugit.data;

import android.database.Cursor;

import org.threeten.bp.OffsetDateTime;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("SELECT * FROM event ORDER BY toDoDay, priority DESC")
    DataSource.Factory<Integer, Event> getAllEvents();
//    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM event WHERE archived = 0 ORDER BY priority DESC") //Attention: Boolean in SQLite is stored as integer (0 = false; 1 = true)
    List<Event> getAllEventsInListByPriority();

    @Query("SELECT * FROM event WHERE archived=:archived ORDER BY priority DESC")
    DataSource.Factory<Integer, Event> getEventsByArchiveState(boolean archived);

    @Query("SELECT * FROM event WHERE id = :id")
    LiveData<Event> getEvent(int id);

    @Query("SELECT * FROM event WHERE toDoDay = :toDoDay AND archived = 0 ORDER BY toDoDay DESC")
    DataSource.Factory<Integer, Event> getEventsByToDoDay(int toDoDay);

    @Query("SELECT * FROM event WHERE parent_id = :parent_id AND archived = :archived ORDER BY priority DESC")
    DataSource.Factory<Integer, Event> getEventsOfParent(int parent_id, boolean archived);
//    LiveData<List<Event>> getChildEvents(int parent_id);

    @Query("UPDATE event SET archived = :archived WHERE id = :id")
    void setArchiveState(int id, boolean archived);

    @Query("UPDATE event SET done = :done, done_date_time = :doneDateTime WHERE id = :id")
    void setDone(int id, boolean done, OffsetDateTime doneDateTime);

    @Query("SELECT * FROM event WHERE archived = :archived ORDER BY priority DESC")
    Cursor getEventsByPriority(boolean archived);

    @Query("UPDATE event SET toDoDay = :toDoDay WHERE id = :id")
    void setToDoDay(int id, int toDoDay);

}
