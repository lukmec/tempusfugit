package de.lumdev.tempusfugit.util;

import de.lumdev.tempusfugit.data.Event;

//(implemented observer/ listener pattern according to https://code.tutsplus.com/tutorials/android-design-patterns-the-observer-pattern--cms-28963)
public interface EventObservable {
//    void registerObserver(SaveEventDoneObserver saveEventDoneObserver);
//    void removeObserver(SaveEventDoneObserver saveEventDoneObserver);
//    void notifyObservers(int eventId, boolean done);
    void registerObserver(EventObserver eventObserver);
    void removeObserver(EventObserver eventObserver);
    void notifyObserversEventDone(Event event, boolean done);
    void notifyObserversActionEditEvent(Event event);
}