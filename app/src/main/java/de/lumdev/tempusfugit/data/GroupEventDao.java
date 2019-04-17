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

    @Query("SELECT * FROM group_event WHERE archived= :archived ORDER BY id ASC")
//    LiveData<List<GroupEvent>> getGroupEventsByArchiveState(boolean archived);
    DataSource.Factory<Integer, GroupEvent> getGroupEventsByArchiveState(boolean archived);

    @Query("SELECT * FROM group_event WHERE id = :id")
    LiveData<GroupEvent> getGroupEvent(int id);

//    @Query("SELECT * FROM group_event WHERE parent_id = :parent_id ORDER BY id ASC")
//    LiveData<List<GroupEvent>> getChildGroupEvents(int parent_id);

    //query is currently not needed
    @Query("UPDATE group_event SET progress = round((SELECT count(event.id) FROM event WHERE event.parent_id = :id AND event.done ='1')/((SELECT count(event.id) FROM event WHERE event.parent_id = :id)*1.0)*100) WHERE id = :id")
    void calcProgress(int id);

    @Query("UPDATE group_event SET archived = :archived WHERE id = :id")
    void setArchiveState(int id, boolean archived);
}
