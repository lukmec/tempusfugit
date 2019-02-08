package de.lumdev.tempusfugit.util;

import de.lumdev.tempusfugit.data.Event;

public interface EventObserver {
    void onEventDone(Event event, boolean done);
    void onActionEditEvent(Event event);
}
