package de.lumdev.tempusfugit.data;


import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import de.lumdev.tempusfugit.R;

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
    public int parentId; //parent means the groupEvent the event belongs to

    @ColumnInfo(name = "due_date")
    public OffsetDateTime dueDate;

    public Duration duration;

    public double importance;

    public double urgency;

    public double priority;

    public boolean visible;

    public int color;

    public Event(String name, String description, int parentId){
        this.name = name;
        this.description = description;
        this.done = false;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        this.parentId = parentId;
        this.duration = Duration.ofMinutes(10);
        this.importance = 1.0;
        this.urgency = 1.0;
        this.priority = this.importance * this.urgency;
        this.visible = true;
        this.color = 4498259; //44a353 (hex) == 4498259 (dec) (color is green)
    }

    @Ignore
    public Event deepcopy(){
        Event newE = new Event(this.name, this.description, this.parentId);
        newE.id = this.id;
        newE.name = this.name;
        newE.description = this.description;
        newE.creationDate = this.creationDate;
        newE.done = this.done;
        newE.doneDateTime = this.doneDateTime;
        newE.parentId = this.parentId;
        newE.dueDate = this.dueDate;
        newE.duration = this.duration;
        newE.importance = this.importance;
        newE.urgency = this.urgency;
        newE.priority = this.priority;
        newE.visible = this.visible;
        newE.color = this.color;
        return newE;
    }
}
