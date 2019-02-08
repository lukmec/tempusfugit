package de.lumdev.tempusfugit.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GroupEventDao {

    @Insert
    void insert(GroupEvent groupEvent);

    @Update
    void update(GroupEvent groupEvent);

    @Delete
    void delete(GroupEvent groupEvent);

    @Query("DELETE FROM group_event")
    void deleteAll();

    @Query("SELECT * FROM group_event ORDER BY id ASC")
    DataSource.Factory<Integer, GroupEvent> getAllGroupEvents();
//    LiveData<List<GroupEvent>> getAllGroupEvents();

    @Query("SELECT * FROM group_event WHERE visible='true' ORDER BY id ASC")
    LiveData<List<GroupEvent>> getVisibleGroupEvents();

    @Query("SELECT * FROM group_event WHERE id = :id")
    LiveData<GroupEvent> getGroupEvent(int id);

//    @Query("SELECT * FROM group_event WHERE parent_id = :parent_id ORDER BY id ASC")
//    LiveData<List<GroupEvent>> getChildGroupEvents(int parent_id);
}
