package de.lumdev.tempusfugit.util;

import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;

public interface GroupEventObserver {
    void onClickGroupEvent(GroupEvent groupEvent);
    void onLongClickGroupEvent(GroupEvent groupEvent);
}
