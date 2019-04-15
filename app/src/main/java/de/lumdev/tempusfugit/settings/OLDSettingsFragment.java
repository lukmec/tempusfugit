package de.lumdev.tempusfugit.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import de.lumdev.tempusfugit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OLDSettingsFragment extends Fragment {

    private TabLayout tabLayout;
    private FloatingActionButton fab;

    public OLDSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.__old__fragment_settings, container, false);
        //get general views
//        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
//        tabLayout.setVisibility(View.GONE); //hide navigation from user while editing group event
//        fab = getActivity().findViewById(R.id.fab_main);
//        fab.hide(); //hide FAB on screen

        return rootView;
    }

}
