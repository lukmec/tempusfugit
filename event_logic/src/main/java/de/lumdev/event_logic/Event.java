package de.lumdev.event_logic;

/**
 * Created by LukasSurface on 12.04.2018.
 */

public interface Event {

    public String getTitle();
    public String getDescription();
    public Event getParent();
    public boolean isDone();

    public String toString();
}
