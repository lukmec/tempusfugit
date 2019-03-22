package de.lumdev.tempusfugit;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.QuickDirtyDataHolder;
import de.lumdev.tempusfugit.util.GroupEventObserver;
import de.lumdev.tempusfugit.util.SelectGEDialogObservable;
import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

public class SelectGroupEventDialogFragment extends DialogFragment {

    private MainViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        GroupEventAdapter adapter = new GroupEventAdapter(getContext()); //previous getActivity()
        viewModel.getAllGroupEvents().observe(this, adapter::submitList);

        RecyclerView mRecyclerView = new RecyclerView(getContext());
        // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPadding(20,30,20,10);

        adapter.registerObserver(new GroupEventObserver() {
            @Override
            public void onClickGroupEvent(GroupEvent groupEvent) {
                //notify observer (observer is responsible for dismissing dialog!!)
                QuickDirtyDataHolder.getInstance().notifySelectGEDialogObservers(groupEvent);
            }
            @Override
            public void onLongClickGroupEvent(GroupEvent groupEvent) {
                //do nothing here
            }
        });

        // Using the Builder class for convenient dialog construction
        return new AlertDialog.Builder(getContext()) //previous getActivity()
                .setTitle(R.string.title_dialog_select_parent_group_event)
                .setView(mRecyclerView)
                .create();
    }

}
