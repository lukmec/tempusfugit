package de.lumdev.tempusfugit.data;

import android.util.Log;

import java.util.ArrayList;

import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

public class QuickDirtyDataHolder {
    private static final QuickDirtyDataHolder ourInstance = new QuickDirtyDataHolder();

    private ArrayList<SelectGEDialogObserver> selectGEDialogObservers;

    public static QuickDirtyDataHolder getInstance() {
        return ourInstance;
    }

    private QuickDirtyDataHolder() {
        selectGEDialogObservers = new ArrayList<>();
    }


    public void addSelectGEDialogObserver(SelectGEDialogObserver observer){
        if(!selectGEDialogObservers.contains(observer)){
            selectGEDialogObservers.add(observer);
//            Log.d("-->", "Observer added.");
//            Log.d("-->", "Number of Observers: "+selectGEDialogObservers.size());
        }
    }
    public void removeSelectGEDialogObserver(SelectGEDialogObserver observer){
        if(selectGEDialogObservers.contains(observer)){
            selectGEDialogObservers.remove(observer);
        }
    }
    public void notifySelectGEDialogObservers(GroupEvent groupEvent){
//        Log.d("-->", "Notify Observers.");
        for (SelectGEDialogObserver observer : selectGEDialogObservers) {
//            Log.d("-->", "Observer notified.");
            observer.onClickGroupEvent(groupEvent);
        }
    }

}
