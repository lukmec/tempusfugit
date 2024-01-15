package de.lumdev.tempusfugit.fragments;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import de.lumdev.tempusfugit.R;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutThisAppFragment extends Fragment {

    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private OnBackPressedCallback backPressedCallback;

    public AboutThisAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_about_this_app, container, false);
        //get general views
//        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
//        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
//        fab = getActivity().findViewById(R.id.fab_main);
//        fab.hide(); //hide FAB on screen

        //register onBackPressedCallback
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backPressedCallback);

        return rootView;
    }

    //disable options menu in toolbar
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //handle back press
        backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //handle back press here
                //use next line if you just need navigate up
                assert getParentFragment() != null;
                NavHostFragment.findNavController(getParentFragment()).navigateUp();
                toolbar.setNavigationIcon(null);
                //Log.e(getClass().getSimpleName(), "handleOnBackPressed");
            }
        };
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
//        getActivity().addOnBackPressedCallback(getViewLifecycleOwner(),this);
        //setting icon for navigating back in toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = activity.findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.toolbar_label_about_app);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationContentDescription(R.string.toolbar_settings_navigation_description);
        toolbar.setNavigationOnClickListener((View v) -> {
            NavHostFragment.findNavController(this).navigateUp();
            toolbar.setNavigationIcon(null); //disbale icon in toolbar again
        });
    }
//    @Override
//    public boolean handleOnBackPressed() {
//        //Do your job here
//        //use next line if you just need navigate up
//        NavHostFragment.findNavController(this).navigateUp();
//        toolbar.setNavigationIcon(null);
//        //Log.e(getClass().getSimpleName(), "handleOnBackPressed");
//        return true;
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        getActivity().removeOnBackPressedCallback(this);
        //unregister listener here
        backPressedCallback.remove();
    }

}
