package de.lumdev.tempusfugit;


import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.icondialog.IconHelper;

import org.threeten.bp.Duration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.data.QuickDirtyDataHolder;
import de.lumdev.tempusfugit.util.SelectGEDialogObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener createOrEditEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createOrUpdateEvent(v);
        }
    };

    //views for event
    private EditText eT_name;
    private EditText eT_description;
    private CardView cV_container_parent_ge;
    private TextView tV_parent_ge;
    private SeekBar sB_importance;
    private SeekBar sB_urgency;
    private TextView tV_duration_minutes;
    private TextView tV_duration_hours;
    private SeekBar sB_duration_minutes;
    private SeekBar sB_duration_hours;
    private Button btn_due_date_date;

    private int givenEventId;
    private int givenParentGroupEventId;
    private int newParentGroupEventId;
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
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.dest_edit_event);
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
        fab = getActivity().findViewById(R.id.fab_main);
        fab.show();
        fab.setOnClickListener(createOrEditEventOnClickListener);

        //get Views related to event
        eT_name = rootView.findViewById(R.id.eT_edit_E_name);
        eT_description = rootView.findViewById(R.id.eT_edit_E_description);
        cV_container_parent_ge = rootView.findViewById(R.id.crdVw_edit_E_container_parentGroupEvent);
        tV_parent_ge = rootView.findViewById(R.id.tV_edit_E_parentGroupEvent);
        sB_importance = rootView.findViewById(R.id.sB_edit_E_importance);
        sB_urgency = rootView.findViewById(R.id.sB_edit_E_urgency);
        tV_duration_minutes = rootView.findViewById(R.id.tV_edit_E_duration_minutes_value);
        tV_duration_hours = rootView.findViewById(R.id.tV_edit_E_duration_hours_value);
        sB_duration_minutes = rootView.findViewById(R.id.sB_edit_E_duration_minutes);
        sB_duration_hours = rootView.findViewById(R.id.sB_edit_E_duration_hours);
        btn_due_date_date = rootView.findViewById(R.id.btn_edit_E_dueDate_setDate);

        //set inital values
        tV_duration_minutes.setText(String.valueOf(sB_duration_minutes.getProgress()*5));
        tV_duration_hours.setText(String.valueOf(sB_duration_hours.getProgress()));

        //set listeners
        cV_container_parent_ge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.slct_ge_dest);
//                viewModel.setNavController(Navigation.findNavController(v));

                SelectGroupEventDialogFragment selectGroupEventFragment = new SelectGroupEventDialogFragment();

                QuickDirtyDataHolder.getInstance().addSelectGEDialogObserver(new SelectGEDialogObserver() {
                    //observer which helps detecting on which item (groupevent) the user clicked (in SelectGroupEventDialog)
                    @Override
                    public void onClickGroupEvent(GroupEvent groupEvent) {
//                        toast("You clicked on GroupEvent with ID: "+groupEvent.id);
                        newParentGroupEventId = groupEvent.id;
                        selectGroupEventFragment.dismiss();
                        //update UI with GroupEvent Info
                        cV_container_parent_ge.setCardBackgroundColor(groupEvent.color);
                        tV_parent_ge.setText(groupEvent.name);
                        setIcon(groupEvent.icon, tV_parent_ge);
                    }
                });

                selectGroupEventFragment.show(getFragmentManager(), "selectGroupEventDialog");

            }
        });
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
            givenEventId = EditEventFragmentArgs.fromBundle(getArguments()).getEventId();
            givenParentGroupEventId = EditEventFragmentArgs.fromBundle(getArguments()).getParentGroupEvent();
            newParentGroupEventId = givenParentGroupEventId;
        } else {
            givenEventId = -1;
            givenParentGroupEventId = -1;
            newParentGroupEventId = givenParentGroupEventId;
        }

        //set values of fields, according to provided event
        if (givenEventId != -1 && givenParentGroupEventId != -1) {
            //retrieve groupEvent
            LiveData<Event> selectedEvent = viewModel.getEvent(givenEventId);
            selectedEvent.observe(this, event -> {
                //save GroupEvent (as Deepcopy object) for later use
                eventToEdit = event.deepcopy();
                //update UI
                eT_name.setText(event.name);
                eT_description.setText(event.description);
                sB_importance.setProgress((int)Math.round(event.importance));
                sB_urgency.setProgress((int)Math.round(event.urgency));

                Duration duration = event.duration;
                long duration_in_minutes = duration.toMinutes();
                long minutes = duration_in_minutes % 60;
                long hours = duration_in_minutes / 60;
                tV_duration_minutes.setText(String.valueOf(minutes));
                tV_duration_hours.setText(String.valueOf(hours));
                sB_duration_minutes.setProgress(Math.round(minutes/5f));
                if (hours <= 12) sB_duration_hours.setProgress((int)hours);

            });
            LiveData<GroupEvent> selectedGroupEvent = viewModel.getGroupEvent(givenParentGroupEventId);
            selectedGroupEvent.observe(this, groupEvent -> {
                //update UI
                cV_container_parent_ge.setCardBackgroundColor(groupEvent.color);
                tV_parent_ge.setText(groupEvent.name);
                setIcon(groupEvent.icon, tV_parent_ge);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_check_black_24dp);
    }

    public void createOrUpdateEvent(View v) {
        if (isValidInput()) {
            if (givenEventId != -1){
                eventToEdit.name = eT_name.getText().toString();
                eventToEdit.description = eT_description.getText().toString();
                eventToEdit.parentId = this.newParentGroupEventId;
                eventToEdit.importance = sB_importance.getProgress();
                eventToEdit.urgency = sB_urgency.getProgress();
                eventToEdit.duration = Duration.ofMinutes(sB_duration_minutes.getProgress()*5).plusHours(sB_duration_hours.getProgress());
                viewModel.updateEvent(eventToEdit);
            }else {
                Event event = new Event(eT_name.getText().toString(), eT_description.getText().toString(), this.newParentGroupEventId, sB_importance.getProgress(), sB_urgency.getProgress(), Duration.ofMinutes(sB_duration_minutes.getProgress()*5).plusHours(sB_duration_hours.getProgress()));
                viewModel.insertEvent(event);
            }
            fab.hide();
            hideKeyboardFrom(v.getContext(), v);
            NavHostFragment.findNavController(this).navigateUp();
        }else{
            //displaying error messages to user is done in this.isValidInput()
        }
    }

    private boolean isValidInput(){
        if (eT_name.getText().toString().equals("")) {
            toast(getResources().getString(R.string.err_invalid_input_missing_name, getResources().getString(R.string.event)));
            return false;
        }
        if (newParentGroupEventId <= 0) {
            toast(getResources().getString(R.string.err_invalid_input_parent_element, getResources().getString(R.string.group_event), getResources().getString(R.string.event)));
            return false;
        }
        if(sB_duration_minutes.getProgress() == 0 && sB_duration_hours.getProgress() == 0) {
            toast(getResources().getString(R.string.err_invalid_duration, getResources().getString(R.string.event)));
            return false;
        }
        return true;
    }

    public void toast(String textToToast){
        //added check for NULL after facing issues and reading http://justmobiledev.com/resolving-fragment-context-loss-issues/
        if (this.getContext() != null) {
            Toast.makeText(getContext(), textToToast, Toast.LENGTH_SHORT).show();
        }
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
//                Toast.makeText(context, "err: Failed to display icon", Toast.LENGTH_SHORT).show();
                toast("err: Failed to display icon");
            }
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        //implemented after reading after https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

