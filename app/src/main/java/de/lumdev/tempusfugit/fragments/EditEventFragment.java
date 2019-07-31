package de.lumdev.tempusfugit.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import androidx.preference.PreferenceManager;
import de.lumdev.tempusfugit.fragments.EditEventFragmentArgs;
import de.lumdev.tempusfugit.MainViewModel;
import de.lumdev.tempusfugit.R;
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
    private FloatingActionButton fabMain;
    private FloatingActionButton fabArchive;
    private FloatingActionButton fabDelete;
    private View.OnClickListener createOrEditEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createOrUpdateEvent(v);
        }
    };
    private View.OnClickListener archiveEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setArchiveStateOfEvent(v);
        }
    };
    private View.OnClickListener deleteEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteEvent(v);
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
//        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
//        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
        fabMain = rootView.findViewById(R.id.fab_edt_e);
        fabArchive = rootView.findViewById(R.id.fab_arch_e);
        fabDelete = rootView.findViewById(R.id.fab_del_e);
//        fabMain.show();
        fabMain.setOnClickListener(createOrEditEventOnClickListener);
        fabArchive.setOnClickListener(archiveEventOnClickListener);
        fabDelete.setOnClickListener(deleteEventOnClickListener);

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
        tV_parent_ge.setTextColor(ContextCompat.getColor(getContext(), R.color.onPrimary));

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
                        tV_parent_ge.setTextColor(groupEvent.textColor);
                        setIcon(groupEvent.icon, tV_parent_ge, groupEvent.textColor);
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

        //try to get defaultParentGroupEvent
        if(newParentGroupEventId == -1){
            //meaning parentGroupEvent is not explicitly given --> select default groupEvent from Preferences (if set there)
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity() /* Activity context */);
            String defaultGroupEventName = sharedPreferences.getString(getString(R.string.pref_id_default_group_event_name), getString(R.string.pref_default_group_event_none_selected));
            int defaultGroupEventId = sharedPreferences.getInt(getString(R.string.pref_id_default_group_event_id), -1);
//            Log.d("--->", defaultGroupEventName);
//            Log.d("--->", ""+defaultGroupEventId);
            //set parentGroupEventId in UI to default from SharedPreferences (if it exists)
            if(defaultGroupEventId != -1){
                newParentGroupEventId = defaultGroupEventId;
            }
        }

        //set values of fields, according to provided groupEvent
        if (newParentGroupEventId != -1){
            LiveData<GroupEvent> selectedGroupEvent = viewModel.getGroupEvent(newParentGroupEventId);
            selectedGroupEvent.observe(this, groupEvent -> {
                //update UI
                cV_container_parent_ge.setCardBackgroundColor(groupEvent.color);
                tV_parent_ge.setText(groupEvent.name);
                tV_parent_ge.setTextColor(groupEvent.textColor);
                setIcon(groupEvent.icon, tV_parent_ge, groupEvent.textColor);
            });
        }
        //set values of fields, according to provided event
        if (givenEventId != -1) {
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

                if (! event.archived){
                    fabDelete.hide();
                }
                if (event.archived){
                    boolean elementsDeletable = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getResources().getString(R.string.pref_id_elements_deletable), true);
                    if (elementsDeletable) {
                        fabDelete.show();
                    }
                    fabArchive.setImageResource(R.drawable.ic_unarchive_white_24dp);
                    fabArchive.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_unarchive)));
                }
            });
        }else{ //meaning eventId == -1 (Event not created yet)
            //disable possibility to archived group event while in creation of new
            fabArchive.hide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fabMain.setImageResource(R.drawable.ic_check_black_24dp);
    }

    public void createOrUpdateEvent(View v) {
        if (isValidInput()) {
            if (givenEventId != -1){
                //update event
                eventToEdit.name = eT_name.getText().toString();
                eventToEdit.description = eT_description.getText().toString();
                eventToEdit.parentId = this.newParentGroupEventId;
                eventToEdit.importance = sB_importance.getProgress();
                eventToEdit.urgency = sB_urgency.getProgress();
                eventToEdit.duration = Duration.ofMinutes(sB_duration_minutes.getProgress()*5).plusHours(sB_duration_hours.getProgress());
                viewModel.updateEvent(eventToEdit);
            }else {
                //insert new event
                Event event = new Event(eT_name.getText().toString(), eT_description.getText().toString(), this.newParentGroupEventId, sB_importance.getProgress(), sB_urgency.getProgress(), Duration.ofMinutes(sB_duration_minutes.getProgress()*5).plusHours(sB_duration_hours.getProgress()));
                viewModel.insertEvent(event);
            }
            viewModel.calculateToDoDateOfEvents(getContext()); //re calculate List of events that have to be today (and which have to be done later)
            fabMain.hide();
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

    private void setArchiveStateOfEvent(View v){
        if (eventToEdit != null){
            if (!eventToEdit.archived) {
                viewModel.setEventArchivedState(eventToEdit.id, true);
                fabArchive.setImageResource(R.drawable.ic_unarchive_white_24dp);
                fabArchive.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_unarchive)));
                toast(getString(R.string.toast_element_archived, getString(R.string.event)));
            }
            if (eventToEdit.archived) {
                viewModel.setEventArchivedState(eventToEdit.id, false);
                fabArchive.setImageResource(R.drawable.ic_archive_white_24dp);
                fabArchive.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_archive)));
                toast(getString(R.string.toast_element_unarchived, getString(R.string.event)));
            }
//            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private void deleteEvent(View v){
        if (eventToEdit != null){

                //show dialog for checking whether user is sure
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setMessage(getString(R.string.dialog_delete_question, getString(R.string.event)));
                builder.setPositiveButton(R.string.dialog_delete_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked YES button (event shall be deleted)
                        NavHostFragment.findNavController(getParentFragment()).navigateUp(); //navigate up, in order to prevent from displaying page, where shown data is deleted
                        viewModel.deleteEvent(eventToEdit.id); //delete actual data (after navigating to a page, where data isn't needed any more)
                        toast(getString(R.string.toast_element_deleted, getString(R.string.event)));
                    }
                });
                builder.setNegativeButton(R.string.dialog_delete_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
//            NavHostFragment.findNavController(this).navigateUp();
    }

    private void setIcon(int iconId, TextView setIconInThisView, int iconColor){
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
        DrawableCompat.setTint(setIconInThisView.getCompoundDrawables()[2],iconColor);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        //implemented after reading after https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

