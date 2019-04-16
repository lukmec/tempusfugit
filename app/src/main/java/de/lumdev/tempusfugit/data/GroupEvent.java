package de.lumdev.tempusfugit.data;

import android.util.Log;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import de.lumdev.tempusfugit.R;
import de.lumdev.tempusfugit.util.MaterialColorHelper;

@Entity(tableName = "group_event")
public class GroupEvent {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "creation_date")
    public OffsetDateTime creationDate;

    public String name;

    public String description;

    public int color;

    public int textColor;

    public int icon;

    @ColumnInfo(name = "parent_id")
    public int parentId;

    public boolean archived;

    public int progress;

    @Ignore
    public GroupEvent(String name, String description){
        this.name = name;
        this.description = description;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        //defaut values
        this.color = 4498259; //44a353 (hex) == 4498259 (dec) (color is green)
        this.textColor = 0xFF000000; //0 (hex) == 0 (dec) (color is black)
        this.icon = R.drawable.ic_directions_car_black_24dp;
        this.parentId = -1;
        this.archived = false;
        this.progress = 0;
    }

    public GroupEvent(String name, String description, int color, int icon, int parentId){
        this.name = name;
        this.description = description;
        this.creationDate = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
        this.color = color;
//        this.textColor = 0xFF000000; //0 (hex) == 0 (dec) (color is black)
        this.textColor = 0xFFFFFFFF; // (color is white)
//        Log.d("--->", String.valueOf(MaterialColorHelper.getTextColor(color, MaterialColorHelper.RETURN_COLOR)));
//        this.textColor = MaterialColorHelper.getTextColor(color, MaterialColorHelper.RETURN_COLOR);
        this.icon = icon;
        this.parentId = parentId;
        this.archived = false;
        this.progress = 0;
    }

    @Ignore
    public GroupEvent deepcopy(){
        GroupEvent newGE = new GroupEvent(this.name, this.description);
        newGE.id = this.id;
        newGE.name = this.name;
        newGE.description = this.description;
        newGE.creationDate = this.creationDate;
        newGE.color = this.color;
        newGE.textColor = this.textColor;
        newGE.icon = this.icon;
        newGE.parentId = this.parentId;
        newGE.archived = this.archived;
        newGE.progress = this.progress;
        return newGE;
    }

}
