package de.lumdev.tempusfugit.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceFragmentCompat;
import de.lumdev.tempusfugit.R;

public class SettingsTemplateFragment extends PreferenceFragmentCompat implements OnBackPressedCallback {

    private Toolbar toolbar;
    private int toolbarTitleResId = 0;

    //this method always has to be implemented by Child-classes
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //child-classes typically call following two methods:
//        setPreferencesFromResource(R.xml.pref_main, rootKey);
//        setupToolbar(R.string.toolbar_settings_title);
    }

    void setupToolbar(int toolbarTitleResId){
        this.toolbarTitleResId = toolbarTitleResId;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = activity.findViewById(R.id.toolbar_main);
        toolbar.setTitle(this.toolbarTitleResId);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationContentDescription(R.string.toolbar_settings_navigation_description);
        toolbar.setNavigationOnClickListener((View v) -> {
            NavHostFragment.findNavController(this).navigateUp();
            toolbar.setNavigationIcon(null); //disbale icon in toolbar again
        });
    }

    //disable options menu in toolbar
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //hide options menu by not showing all of its menu-items
        for (int i = 0; i < menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }
        //as alternative implementtion
        //        menu.findItem(R.id.settings_dest).setVisible(false);
        //        menu.findItem(R.id.abt_app_dest).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    // used for handling back press (on physical/ soft button); see: https://stackoverflow.com/questions/51043428/handling-back-button-in-android-navigation-component
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().addOnBackPressedCallback(getViewLifecycleOwner(),this);
    }
    @Override
    public boolean handleOnBackPressed() {
        //Do your job here
        //use next line if you just need navigate up
        NavHostFragment.findNavController(this).navigateUp();
        toolbar.setNavigationIcon(null);
        //Log.e(getClass().getSimpleName(), "handleOnBackPressed");

        return true;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().removeOnBackPressedCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toolbarTitleResId != 0){
            setupToolbar(this.toolbarTitleResId);
        }
    }
}