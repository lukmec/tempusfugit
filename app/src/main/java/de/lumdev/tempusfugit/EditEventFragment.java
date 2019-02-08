package de.lumdev.tempusfugit;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.icondialog.IconHelper;

import org.threeten.bp.Duration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener newGroupEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createNewEvent(v);
        }
    };

    //views for event
    private EditText eT_name;
    private EditText eT_description;
    private CardView cV_container_parent_ge;
    private TextView tV_parent_ge;
    private RadioGroup rG_importance;
    private RadioGroup rG_urgency;
    private TextView tV_duration_minutes;
    private TextView tV_duration_hours;
    private SeekBar sB_duration_minutes;
    private SeekBar sB_duration_hours;
    private Button btn_due_date_date;

    private int eventId;
    private int parentGroupEvent;
    private Event eventToEdit = null;

    public EditEventFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_edit_event, container, false);
        //get general views
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
        fab = getActivity().findViewById(R.id.fab_main);
        fab.setOnClickListener(newGroupEventOnClickListener);

        //get Views related to event
        eT_name = rootView.findViewById(R.id.eT_edit_E_name);
        eT_description = rootView.findViewById(R.id.eT_edit_E_description);
        cV_container_parent_ge = rootView.findViewById(R.id.crdVw_edit_E_container_parentGroupEvent);
        tV_parent_ge = rootView.findViewById(R.id.tV_edit_E_parentGroupEvent);
        rG_importance = rootView.findViewById(R.id.rG_edit_E_importance);
        rG_urgency = rootView.findViewById(R.id.rG_edit_E_urgency);
        tV_duration_minutes = rootView.findViewById(R.id.tV_edit_E_duration_minutes_value);
        tV_duration_hours = rootView.findViewById(R.id.tV_edit_E_duration_hours_value);
        sB_duration_minutes = rootView.findViewById(R.id.sB_edit_E_duration_minutes);
        sB_duration_hours = rootView.findViewById(R.id.sB_edit_E_duration_hours);
        btn_due_date_date = rootView.findViewById(R.id.btn_edit_E_dueDate_setDate);

        //set inital values
        tV_duration_minutes.setText(String.valueOf(sB_duration_minutes.getProgress()*5));
        tV_duration_hours.setText(String.valueOf(sB_duration_hours.getProgress()));
        //set listeners
        sB_duration_minutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tV_duration_minutes.setText(String.valueOf(progress*5));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sB_duration_hours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tV_duration_hours.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get typesafe Argument from (auto generted class from) androidx.navigation
        //if no arguments given id will be -1. However argument given can also be -1 (meaning no groupEvent specified)
        if (getArguments() != null) {
            eventId = EditEventFragmentArgs.fromBundle(getArguments()).getEventId();
            parentGroupEvent = EditEventFragmentArgs.fromBundle(getArguments()).getParentGroupEvent();
        } else {
            eventId = -1;
            parentGroupEvent = -1;
        }

        //set values of fields, according to provided event
        if (eventId != -1 && parentGroupEvent != -1) {
            //retrieve groupEvent
            LiveData<Event> selectedEvent = viewModel.getEvent(eventId);
            selectedEvent.observe(this, event -> {
                //save GroupEvent (as Deepcopy object) for later use
                eventToEdit = event.deepcopy();
                //update UI
                eT_name.setText(event.name);
                eT_description.setText(event.description);
                Duration duration = event.duration;
                long minutes = duration.toMinutes();
                long hours = duration.toHours();
                tV_duration_minutes.setText(String.valueOf(minutes));
                tV_duration_hours.setText(String.valueOf(hours));
                sB_duration_minutes.setProgress(Math.round(minutes/5f));
                if (hours <= 12) sB_duration_hours.setProgress((int)hours);

            });
            LiveData<GroupEvent> selectedGroupEvent = viewModel.getGroupEvent(parentGroupEvent);
            selectedGroupEvent.observe(this, groupEvent -> {
                //update UI
                cV_container_parent_ge.setCardBackgroundColor(groupEvent.color);
                setIcon(groupEvent.icon, tV_parent_ge);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_check_black_24dp);
    }

    public void createNewEvent(View v) {
        if(isValidInput()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private boolean isValidInput(){
        return true;
    }

    public void toast(String textToToast){
        Toast.makeText(getActivity().getApplicationContext(), textToToast, Toast.LENGTH_SHORT).show();
    }

    private void setIcon(int iconId, TextView setIconInThisView){
        Context context = getContext();
        IconHelper iconHelper = IconHelper.getInstance(context);
        try{
            //IconPicker allows "0" as ID, but .setImageRessource justs sets no icon for ID=0, instead of throwing exception
            //--> Raising custom Exception, when ID=0 in order to let the IconHelper display correct Icon
            if (iconId == 0){
                throw new Exception("err: IconId=0, catching special case...");
            }
//            setIconInThisView.setCompoundDrawables(null, null, context.getDrawable(iconId),null);
//            setIconInThisView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,iconId,0);
            setIconInThisView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, context.getDrawable(iconId),null);
        }catch (Exception e){
            try {
                iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                    @Override
                    public void onDataLoaded() {
                        // This happens on UI thread, and is guaranteed to be called.
                        Drawable iconDrawable = iconHelper.getIcon(iconId).getDrawable(context);
//                        setIconInThisView.setCompoundDrawables(null, null, iconDrawable,null);
                        setIconInThisView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, iconDrawable,null);
                    }
                });
            }catch (Exception e2){
                Toast.makeText(context, "err: Failed to display icon", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

