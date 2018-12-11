package de.lumdev.event_logic;

import java.util.ArrayList;

/**
 * Created by LukasSurface on 12.04.2018.
 */

public class AllEvents {
    private static final AllEvents ourInstance = new AllEvents();

    private ArrayList<Event> allEvents;

    public static AllEvents getInstance() {
        return ourInstance;
    }

    private AllEvents() {
        this.allEvents = new ArrayList<>();
    }

    public void addEvent(Event eventToAdd){
        this.allEvents.add(eventToAdd);
    }
    public String toString(){
        StringBuilder allEventsString = new StringBuilder();
        for (Event e : this.allEvents){
            allEventsString.append(e)
                    .append("\n");
        }
        return allEventsString.toString();
    }
    public ArrayList<Event> getAllEvents() {
        return this.allEvents;
    }
}
