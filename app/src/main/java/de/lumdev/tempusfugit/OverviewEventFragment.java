package de.lumdev.tempusfugit;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.util.EventObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewEventFragment extends Fragment {

    private MainViewModel viewModel;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private View.OnClickListener addEventOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addEvent(v);
        }
    };

    public OverviewEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OverviewEventFragment.
     */
    public static OverviewEventFragment newInstance() {
        OverviewEventFragment fragment = new OverviewEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_overview_event, container, false);

        //get Views
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        tabLayout = getActivity().findViewById(R.id.tabLayout_main);
        tabLayout.setVisibility(View.VISIBLE); //set tabLayout to visible, in order to ensure that user can navigate
        fab = getActivity().findViewById(R.id.fab_main);
        fab.show();
        //set onClickListeners
        fab.setOnClickListener(addEventOnClickListener);

        //bind recyclerView to DataModel
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_events);
        EventAdapter adapter = new EventAdapter(getActivity());
        viewModel.getAllEvents().observe(this, adapter::submitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //prevent short blinking of recyclerview, when data updates
        //see https://stackoverflow.com/questions/29331075/recyclerview-blinking-after-notifydatasetchanged
        recyclerView.getItemAnimator().setChangeDuration(0);
//        recyclerView.setNestedScrollingEnabled(false);

        //regsiter observer, that observes/ lisens to changes of any events "done" state
            //if event is done=true or done=false, the info shall be persisted
        adapter.registerObserver(new EventObserver() {
            @Override
            public void onEventDone(Event event, boolean newDoneState) {
                viewModel.setEventDone(event.id, event.parentId, newDoneState);
            }
            @Override
            public void onActionEditEvent(Event event){
                //navigate to fragment where editing/ creating event is possible
            OverviewEventFragmentDirections.ActionOvrvwEDestToEdtEDest action = OverviewEventFragmentDirections.actionOvrvwEDestToEdtEDest();
            action.setEventId(event.id);
            action.setParentGroupEvent(event.parentId);
            NavHostFragment.findNavController(getParentFragment()).navigate(action);
            }
        });
//        recyclerView.lis

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
    }

    private void addEvent(View v){
        fab.hide();
        NavHostFragment.findNavController(this).navigate(R.id.action_ovrvw_e_dest_to_edt_e_dest);
    }

}
