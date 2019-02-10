package de.lumdev.tempusfugit.util;

import de.lumdev.tempusfugit.data.GroupEvent;

public interface SelectGEDialogObservable {
    void registerObserver(SelectGEDialogObserver selectGEDialogObserver);
    void removeObserver(SelectGEDialogObserver selectGEDialogObserver);
    void notifyObserversOnClickGroupEvent(GroupEvent groupEvent);
}
