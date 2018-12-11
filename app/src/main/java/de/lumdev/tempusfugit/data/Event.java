package de.lumdev.tempusfugit.data;


import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "creation_date")
    public OffsetDateTime creationDate;

    public String name;

    public String description;

    public boolean done;

    @ColumnInfo(name = "done_date_time")
    public OffsetDateTime doneDateTime;

    @ColumnInfo(name = "parent_id")
    public int parentId;

    @ColumnInfo(name = "due_date")
    public OffsetDateTime dueDate;

    public Duration duration;

    public double importance;

    public double urgency;

    public double priority;

    public boolean visible;

    public Event(String name, String description){
        this.name = name;
        this.description = description;
        this.done = false;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        this.parentId = -1;
        this.duration = Duration.ofMinutes(10);
        this.importance = 1.0;
        this.urgency = 1.0;
        this.priority = this.importance * this.urgency;
        this.visible = true;
    }
}
