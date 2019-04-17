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

    public boolean archived;

    public int color;

    @ColumnInfo(name = "text_color")
    public int textColor;

    public int icon;

    public int toDoDay; //day, when event has to be done (0 meaning today, 1 meaning tomorrow, -1 meaning yesterday, etc.)

    @Ignore
    public Event(String name, String description, int parentId){
        this.name = name;
        this.description = description;
        this.done = false;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        this.parentId = parentId;
        this.duration = Duration.ofMinutes(60);
        this.importance = 1.0;
        this.urgency = 1.0;
        this.priority = this.importance + this.urgency;
        this.archived = false;
        //provide color in form 0xAARRGGBB --- Alpha, Red, Green, Blue --- //https://stackoverflow.com/questions/4602902/how-to-set-the-text-color-of-textview-in-code
        //after change of color: deinstall, and reinstall app, in order to polulate db new
        this.color = 4498259; //44a353 (hex) == 4498259 (dec) (color is green)
        this.textColor = 0xFF000000; //0 (hex) == 0 (dec) (color is black) -16777216 == 0xFF000000
        this.icon = R.drawable.ic_directions_car_black_24dp;
        this.toDoDay = 0;
    }

    public Event(String name, String description, int parentId, double importance, double urgency, Duration duration){
        this.name = name;
        this.description = description;
        this.done = false;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        this.parentId = parentId;
        this.duration = duration;
        this.importance = importance;
        this.urgency = urgency;
        this.priority = importance + urgency;
        this.archived = false;
        this.color = 4498259; //44a353 (hex) == 4498259 (dec) (color is green)
        this.textColor = -16777216
        ; //0 (hex) == 0 (dec) (color is black) //http://www.binaryconvert.com/result_signed_int.html?hexadecimal=FF121212
        this.icon = R.drawable.ic_directions_car_black_24dp;
        this.toDoDay = 0;
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
        newE.archived = this.archived;
        newE.color = this.color;
        newE.textColor = this.textColor;
        newE.icon = this.icon;
        newE.toDoDay = this.toDoDay;
        return newE;
    }
}
