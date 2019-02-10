package de.lumdev.tempusfugit;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import de.lumdev.tempusfugit.data.GroupEvent;
import petrov.kristiyan.colorpicker.ColorPicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconHelper;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditGroupEventFragment extends Fragment implements IconDialog.Callback{

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener newGroupEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createOrUpdateGroupEvent(v);
        }
    };
    private EditText eT_name;
    private EditText eT_description;
    private Button btn_color;
    private View.OnClickListener selectColorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectColor(v);
        }
    };
    private ImageButton btn_icon;
    private View.OnClickListener selectIconOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectIcon(v);
        }
    };
    private int pickedColor;
    private Icon[] preselectedIcons;
    private int pickedIconId;
    private boolean iconPicked;
    private int groupEventId;
    private GroupEvent groupEventToEdit = null;

    public EditGroupEventFragment() {
        // Required empty public constructor
    }

    //factory class for creating fragment over fragmenttransaction with fragment manager
    public static EditGroupEventFragment newInstance() {
        return new EditGroupEventFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_edit_group_event, container, false);
        //get Views
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.dest_edit_group_event);
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
        fab = getActivity().findViewById(R.id.fab_main);
        fab.show();
        eT_name = rootView.findViewById(R.id.eT_newGE_name);
        eT_description = rootView.findViewById(R.id.eT_newGE_description);
        btn_color = rootView.findViewById(R.id.btn_newGE_color);
        btn_icon = rootView.findViewById(R.id.btn_nGE_icon);
        pickedColor = 0;
        preselectedIcons = null;
        iconPicked = false;
        //set onClickListeners
        fab.setOnClickListener(newGroupEventOnClickListener);
        btn_color.setOnClickListener(selectColorOnClickListener);
        btn_icon.setOnClickListener(selectIconOnClickListener);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get typesafe Argument from (auto generted class from) androidx.navigation
        //if no arguments given id will be -1. However argument given can also be -1 (meaning no groupEvent specified)
        if (getArguments() != null) {
            groupEventId = EditGroupEventFragmentArgs.fromBundle(getArguments()).getGroupEventId();
        }else{
            groupEventId = -1;
        }

        //set values of fields, according to provided groupEvent
        if (groupEventId != -1){
            //retrieve groupEvent
            LiveData<GroupEvent> selectedGroupEvent = viewModel.getGroupEvent(groupEventId);
            selectedGroupEvent.observe(this, ge -> {
                //save GroupEvent (as Deepcopy object) for later use
                groupEventToEdit = ge.deepcopy();
                //update UI
                eT_name.setText(ge.name);
                eT_description.setText(ge.description);
                pickedColor = ge.color;
                btn_color.setBackgroundColor(pickedColor);
                pickedIconId = ge.icon;
                iconPicked = true;
                setIcon(pickedIconId);
            });
//            //set values in views
//            if (groupEvent != null && groupEvent.getValue() != null) {
//                eT_name.setText(groupEvent.getValue().name);
//                eT_description.setText(groupEvent.getValue().description);
//                btn_color.setBackgroundColor(groupEvent.getValue().color);
//                btn_icon.setImageResource(groupEvent.getValue().icon);
//            }else{
//                toast("err: Could not find GroupEvent.");
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_check_black_24dp);
    }

    public void createOrUpdateGroupEvent(View v){
        if (isValidInput()) {
            if (groupEventId != -1){
//                GroupEvent gEvent = new GroupEvent(eT_name.getText().toString(), eT_description.getText().toString(), pickedColor, pickedIconId, -1);
//                gEvent.id = groupEventId;

                groupEventToEdit.name = eT_name.getText().toString();
                groupEventToEdit.description = eT_description.getText().toString();
                groupEventToEdit.color = pickedColor;
                groupEventToEdit.icon = pickedIconId;

                viewModel.updateGroupEvent(groupEventToEdit);
            }else {
//            GroupEvent gEvent = new GroupEvent(eT_name.getText().toString(), eT_description.getText().toString());
                GroupEvent gEvent = new GroupEvent(eT_name.getText().toString(), eT_description.getText().toString(), pickedColor, pickedIconId, -1);
                viewModel.insertGroupEvent(gEvent);
            }
            fab.hide();
            hideKeyboardFrom(v.getContext(), v);
            NavHostFragment.findNavController(this).navigateUp();
        }else{
            //displaying error messages to user is done in this.isValidInput()
//            toast(getString(R.string.err_invalid_input));
        }
    }

    public void selectColor(View v){
        final ColorPicker colorPicker = new ColorPicker(this.getActivity());
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position,int color) {
                // put code
//                toast("Picked Color: "+color + " pos: "+position);
                pickedColor = color;
                btn_color.setBackgroundColor(color);
            }

            @Override
            public void onCancel(){
                // put code
                toast("Color picking cancelled.");
            }
        })
//        .setColorButtonTickColor(getResources().getColor(R.color.black_de))
                .setColumns(5)
                .setTitle(getString(R.string.colorpicker_select_color))
                .setRoundColorButton(true)
                .show();
    }

    public void selectIcon(View v){

        //FYI: Locale.getDefault() returns "Deutschland" on my phone
//        toast("Locale: "+Locale.getDefault().getDisplayCountry());

        IconDialog iconDialog = new IconDialog();
        iconDialog
                .setSelectedIcons(preselectedIcons)
//        .setMaxSelection(1, false) //default value is 1 --> user can only select 1 icon
                .setShowSelectButton(false)
                //ATTENTION: Search is only provided in english, french, portuguese
                .setSearchEnabled(IconDialog.VISIBILITY_ALWAYS, Locale.getDefault())
                .setTitle(IconDialog.VISIBILITY_NEVER, getString(R.string.iconpicker_select_icon))
                .setTargetFragment(EditGroupEventFragment.this, 0);
        iconDialog
                .show(getFragmentManager(), "icon_dialog"); //---- solution (05.02.2019): https://stackoverflow.com/questions/44203982/why-is-android-o-failing-with-does-not-belong-to-this-fragmentmanager
//                .show(getActivity().getSupportFragmentManager(), "icon_dialog"); //not working but suggestetd in wiki: https://github.com/maltaisn/icondialoglib/wiki/Using-the-dialog
    }
    @Override
    public void onIconDialogIconsSelected(Icon[] icons) {
        //always only take the first picked icon from the dialog (dialog is configured to allow only 1 icon to be selected)
        Icon pickedIcon = icons[0];
        pickedIconId = pickedIcon.getId();
//        toast("IconId: "+pickedIconId);
        iconPicked = true;
//        btn_icon.setImageResource(pickedIcons[0].getId());
        btn_icon.setImageDrawable(pickedIcon.getDrawable(getContext()));
    }

    private void setIcon(int iconId){
        Context context = getContext();
        IconHelper iconHelper = IconHelper.getInstance(context);
        try{
            //IconPicker allows "0" as ID, but .setImageRessource justs sets no icon for ID=0, instead of throwing exception
            //--> Raising custom Exception, when ID=0 in order to let the IconHelper display correct Icon
            if (iconId == 0){
                throw new Exception("err: IconId=0, catching special case...");
            }
            btn_icon.setImageResource(iconId);
        }catch (Exception e){
            try {
                iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                    @Override
                    public void onDataLoaded() {
                        // This happens on UI thread, and is guaranteed to be called.
                        btn_icon.setImageDrawable(iconHelper.getIcon(iconId).getDrawable(context));
                    }
                });
            }catch (Exception e2){
                Toast.makeText(context, "err: Failed to display icon", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidInput(){
        String name = eT_name.getText().toString();
        String description = eT_description.getText().toString();
        if (name.equals("")) {
//            toast("Name: "+name);
            toast(getResources().getString(R.string.err_invalid_input_missing_name, getResources().getString(R.string.group_event)));
            return false;
        }
        //left check out, because not necessary (sometimes description might be left out)
//        if (description.equals("")) {
////            toast("description: "+description);
//            toast(getResources().getString(R.string.err_invalid_input_description, getResources().getString(R.string.event)));
//            return false;
//        }
        if (pickedColor == 0) {
//            toast("color: "+pickedColor);
            toast(getResources().getString(R.string.err_invalid_input_color, getResources().getString(R.string.event)));
            return false;
        }
        if(!iconPicked) {
//            toast("Icon picked: "+ pickedIcon.getId());
            toast(getResources().getString(R.string.err_invalid_input_icon, getResources().getString(R.string.event)));
            return false;
        }
        return true;
    }

    public void toast(String textToToast){
        //added check for NULL after facing issues and reading http://justmobiledev.com/resolving-fragment-context-loss-issues/
        if(this.getContext() != null) {
            Toast.makeText(getActivity().getApplicationContext(), textToToast, Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        //implemented after reading after https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
