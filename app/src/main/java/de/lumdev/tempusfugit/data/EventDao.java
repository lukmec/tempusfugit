package de.lumdev.tempusfugit.data;

import java.util.List;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM event ORDER BY id ASC")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM event WHERE visible='true' ORDER BY id ASC")
    LiveData<List<Event>> getVisibleEvents();

    @Query("SELECT * FROM event WHERE id = :id")
    LiveData<Event> getEvent(int id);

    @Query("SELECT * FROM event WHERE parent_id = :parent_id ORDER BY id ASC")
    LiveData<List<Event>> getChildEvents(int parent_id);

}
