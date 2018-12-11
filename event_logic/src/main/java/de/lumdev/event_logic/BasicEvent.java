package de.lumdev.event_logic;

import java.util.ArrayList;
import java.util.Date;

public class BasicEvent implements Event{

    private int eventId;
    private String title;
    private String description;
    private String place;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private Event parentEvent;
    private boolean done;

    public BasicEvent(int eventId, String title, String description){
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.done = false;

        this.place = null;
        this.startDate = null;
        this.endDate = null;
        this.parentEvent = null;

        AllEvents.getInstance().addEvent(this);
    }


    @Override
    public String getTitle() {
        return this.title;
    }
    @Override
    public String getDescription() {
        return this.description;
    }
    @Override
    public Event getParent() {
        return this.parentEvent;
    }
    @Override
    public boolean isDone() {
        return this.done;
    }
    public String toString(){
        StringBuilder eventString = new StringBuilder();
        eventString.append(this.title.toUpperCase())
                .append(" - ")
                .append(this.description)
                .append(" (")
                .append(this.creationDate)
                .append(")");
        return eventString.toString();
    }
}
