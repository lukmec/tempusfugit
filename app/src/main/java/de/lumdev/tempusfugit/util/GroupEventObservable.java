package de.lumdev.tempusfugit.util;

import de.lumdev.tempusfugit.data.GroupEvent;

public interface GroupEventObservable {
    void registerObserver(GroupEventObserver groupEventObserver);
    void removeObserver(GroupEventObserver groupEventObserver);
    void notifyObserversOnClickGroupEvent(GroupEvent groupEvent);
    void notifyObserversOnLongClickGroupEvent(GroupEvent groupEvent);
}
