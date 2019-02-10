package de.lumdev.tempusfugit.data;

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

    @Query("SELECT * FROM event ORDER BY priority DESC")
    DataSource.Factory<Integer, Event> getAllEvents();
//    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM event WHERE visible='1' ORDER BY priority DESC")
    DataSource.Factory<Integer, Event> getVisibleEvents();
//    LiveData<List<Event>> getVisibleEvents();

    @Query("SELECT * FROM event WHERE id = :id")
    LiveData<Event> getEvent(int id);

    @Query("SELECT * FROM event WHERE parent_id = :parent_id AND visible = :only_visible_events ORDER BY priority DESC")
    DataSource.Factory<Integer, Event> getEventsOfParent(int parent_id, boolean only_visible_events);
//    LiveData<List<Event>> getChildEvents(int parent_id);

    @Query("UPDATE event SET visible = :visible WHERE id = :id")
    void setVisibility(int id, boolean visible);

    @Query("UPDATE event SET done = :done, done_date_time = :doneDateTime WHERE id = :id")
    void setDone(int id, boolean done, OffsetDateTime doneDateTime);

}
