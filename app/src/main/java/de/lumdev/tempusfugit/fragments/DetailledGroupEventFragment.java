package de.lumdev.tempusfugit.fragments;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.fragments.DetailledGroupEventFragmentArgs;
import de.lumdev.tempusfugit.fragments.DetailledGroupEventFragmentDirections;
import de.lumdev.tempusfugit.EventAdapter;
import de.lumdev.tempusfugit.MainViewModel;
import de.lumdev.tempusfugit.R;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.util.EventObserver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.icondialog.IconHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailledGroupEventFragment extends Fragment {

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener createEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createNewEvent(v);
        }
    };
    private int selectedGroupEventId;

    public DetailledGroupEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OverviewEventFragment.
     */
    public static DetailledGroupEventFragment newInstance() {
        DetailledGroupEventFragment fragment = new DetailledGroupEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detailled_group_event, container, false);
        //get Views
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.dest_detailled_group_event);
//        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
//        tabLayout.setVisibility(View.GONE); //set tabLayout to archived, in order to ensure that user can navigate
        fab = rootView.findViewById(R.id.fab_dtl_ge);
//        fab.show();
        //set onClickListeners
        fab.setOnClickListener(createEventOnClickListener);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get typesafe Argument from (auto generted class from) androidx.navigation
        //if no arguments given id will be -1. However argument given can also be -1 (meaning no groupEvent specified)
        if (getArguments() != null) {
            selectedGroupEventId = DetailledGroupEventFragmentArgs.fromBundle(getArguments()).getGroupEventId();
        }else{
            selectedGroupEventId = -1;
        }

        //"WORKAROUND" - set empty adpater to RecyclerView
            //prevents error "RecyclerView: No adapter attached; Skipping layout"
            //correct adapter with data is attached later, when data is available
            //see https://stackoverflow.com/questions/29141729/recyclerview-no-adapter-attached-skipping-layout
//        RecyclerView recyclerView = getActivity().findViewById(R.id.detail_groupEvent_include_event_overview).findViewById(R.id.recycler_events);
//        EventAdapter emptyAdapter = new EventAdapter(getActivity());
//        viewModel.getAllEvents().observe(this, emptyAdapter::submitList);
//        recyclerView.setAdapter(emptyAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.getItemAnimator().setChangeDuration(0);

        //fill includes views with content
        this.onSetCardViewValues();
        this.onSetEventOverviewValues();
    }

//    private getSelectedGroupEvent()

    private void onSetCardViewValues(){
        //get Layout Views
        View includedGroupCard = getActivity().findViewById(R.id.detail_groupEvent_include_group_card);
        TextView name = includedGroupCard.findViewById(R.id.rVCard_groupEventName);
        TextView description = includedGroupCard.findViewById(R.id.rVCard_groupEventDescription);
        TextView progressText = includedGroupCard.findViewById(R.id.rVCard_groupEvent_progressText);
        ProgressBar progress = includedGroupCard.findViewById(R.id.rVCard_groupEventProgress);
        ImageView icon = includedGroupCard.findViewById(R.id.rVCard_groupEventIcon);
        CardView container = includedGroupCard.findViewById(R.id.cardView_groupEvent);

        //get Data from ViewModel & set Data to UI Views
        if (selectedGroupEventId != -1) {
            //retrieve groupEvent
            LiveData<GroupEvent> selectedGroupEvent = viewModel.getGroupEvent(selectedGroupEventId);
            selectedGroupEvent.observe(getViewLifecycleOwner(), groupEvent -> {
                //update UI
                name.setText(groupEvent.name);
                description.setText(groupEvent.description);
                progressText.setText(getResources().getString(R.string.label_cV_groupevent_progress_text, groupEvent.progress));
                progress.setProgress(groupEvent.progress);
                setIcon(groupEvent.icon, icon, groupEvent.textColor);
                //set colors
                name.setTextColor(groupEvent.textColor);
                description.setTextColor(groupEvent.textColor);
                progressText.setTextColor(groupEvent.textColor);

//                progress.getProgressDrawable().setTint(groupEvent.textColor);
                //Source: https://stackoverflow.com/questions/10951978/change-progressbar-color-through-code-only-in-android
                LayerDrawable progressBarDrawable = (LayerDrawable) progress.getProgressDrawable();
//            progressBarDrawable.mutate();
                Drawable backgroundDrawable = progressBarDrawable.getDrawable(0); //id 1 is in my custom prog-bar for the secondary progress
                Drawable progressDrawable = progressBarDrawable.getDrawable(2);
                backgroundDrawable.setColorFilter(ColorUtils.setAlphaComponent(groupEvent.textColor, 90), PorterDuff.Mode.SRC_IN);
//                backgroundDrawable.setColorFilter(MaterialColorHelper.setAlphaToColor(groupEvent.textColor, 90), PorterDuff.Mode.SRC_IN);
                progressDrawable.setColorFilter(groupEvent.textColor, PorterDuff.Mode.SRC_IN);

                container.setCardBackgroundColor(groupEvent.color);
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailledGroupEventFragmentDirections.ActionDtlGeDestToEdtGeDest action = DetailledGroupEventFragmentDirections.actionDtlGeDestToEdtGeDest();
                        action.setGroupEventId(groupEvent.id);
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            });
        }
    }
    private void onSetEventOverviewValues(){
        //get Layout Views
//        View includedEventOverview = getActivity().findViewById(R.id.detail_groupEvent_include_event_overview);
//        RecyclerView recyclerView = includedEventOverview.findViewById(R.id.recycler_events);
        if (getActivity().findViewById(R.id.detail_groupEvent_include_event_overview) == null) Log.d("-->", "include recycler not found by id");
        RecyclerView recyclerView = getActivity().findViewById(R.id.detail_groupEvent_include_event_overview).findViewById(R.id.recycler_events);
        //bind recyclerView to DataModel
        EventAdapter adapter = new EventAdapter(getActivity());
        //get Data from ViewModel
//        viewModel.getAllEvents().observe(this, adapter::submitList);
        viewModel.getAllEventsOfGroup(selectedGroupEventId).observe(getViewLifecycleOwner(), adapter::submitList);
//        recyclerView.swapAdapter(adapter, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getItemAnimator().setChangeDuration(0);
        //regsiter observer, that observes/ lisens to changes of any events "done" state
        //if event is done=true or done=false, the info shall be persisted
        adapter.registerObserver(new EventObserver() {
            @Override
            public void onEventDone(Event event, boolean newDoneState) {
                viewModel.setEventDone(event.id, newDoneState);
            }
            @Override
            public void onActionEditEvent(Event event){
                //navigate to fragment where editing/ creating event is possible
                DetailledGroupEventFragmentDirections.ActionDtlGeDestToEdtEDest action = DetailledGroupEventFragmentDirections.actionDtlGeDestToEdtEDest();
                action.setEventId(event.id);
                action.setParentGroupEvent(event.parentId);
                NavHostFragment.findNavController(getParentFragment()).navigate(action);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
    }

    public void createNewEvent(View v) {
        fab.hide();

        if (selectedGroupEventId != -1) {
            //if groupEventId is set, go into event edit mode with preselcted GroupEvent
            DetailledGroupEventFragmentDirections.ActionDtlGeDestToEdtEDest action = DetailledGroupEventFragmentDirections.actionDtlGeDestToEdtEDest();
//            action.setEventId(-1); //not needed, since -1 is default for eventId
            action.setParentGroupEvent(selectedGroupEventId);
            NavHostFragment.findNavController(getParentFragment()).navigate(action);
        }else{
            //if group event not set, go into edit mode without addional info
            NavHostFragment.findNavController(this).navigate(R.id.action_dtl_ge_dest_to_edt_e_dest);
        }
    }

    private void setIcon(int iconId, ImageView setIconInThisView, int iconColor){
        Context context = getContext();
        IconHelper iconHelper = IconHelper.getInstance(context);
        try{
            //IconPicker allows "0" as ID, but .setImageRessource justs sets no icon for ID=0, instead of throwing exception
            //--> Raising custom Exception, when ID=0 in order to let the IconHelper display correct Icon
            if (iconId == 0){
                throw new Exception("err: IconId=0, catching special case...");
            }
            setIconInThisView.setImageResource(iconId);
        }catch (Exception e){
            try {
                iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                    @Override
                    public void onDataLoaded(IconHelper helper) {
                        // This happens on UI thread, and is guaranteed to be called.
                        setIconInThisView.setImageDrawable(iconHelper.getIcon(iconId).getDrawable(context));
                    }
                });
            }catch (Exception e2){
                Toast.makeText(context, "err: Failed to display icon", Toast.LENGTH_SHORT).show();
            }
        }
        DrawableCompat.setTint(setIconInThisView.getDrawable(),iconColor);
    }
}
