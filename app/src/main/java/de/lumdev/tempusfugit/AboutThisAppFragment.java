package de.lumdev.tempusfugit;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

    public AboutThisAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_about_this_app, container, false);
        //get general views
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
//        fab = getActivity().findViewById(R.id.fab_main);
//        fab.hide(); //hide FAB on screen

        return rootView;
    }

}
