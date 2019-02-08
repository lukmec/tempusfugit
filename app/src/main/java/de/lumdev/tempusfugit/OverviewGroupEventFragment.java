package de.lumdev.tempusfugit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewGroupEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewGroupEventFragment extends Fragment {

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener addGroupEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addGroupEvent(v);
        }
    };

    public OverviewGroupEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OverviewGroupEventFragment.
     */
    public static OverviewGroupEventFragment newInstance() {
        OverviewGroupEventFragment fragment = new OverviewGroupEventFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_overview_group_event, container, false);

        //get Views
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.VISIBLE); //set tabLayout to visible, in order to ensure that user can navigate
        fab = getActivity().findViewById(R.id.fab_main);
        //set onClickListeners
        fab.setOnClickListener(addGroupEventOnClickListener);

        //bind recyclerView to DataModel
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_groupEvents);
        GroupEventAdapter adapter = new GroupEventAdapter(getActivity());
        viewModel.getAllGroupEvents().observe(this, adapter::submitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //prevent short blinking of recyclerview, when data updates
            //see https://stackoverflow.com/questions/29331075/recyclerview-blinking-after-notifydatasetchanged
        recyclerView.getItemAnimator().setChangeDuration(0);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
    }

    private void addGroupEvent(View v){
        NavHostFragment.findNavController(this).navigate(R.id.action_ovrvw_ge_dest_to_edt_ge_dest);
    }

}