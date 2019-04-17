package de.lumdev.tempusfugit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;
import de.lumdev.tempusfugit.util.GroupEventObserver;

public class OverviewArchivedGroupEventFragment extends Fragment implements OnBackPressedCallback {

    private MainViewModel viewModel;
    private Toolbar toolbar;

    public OverviewArchivedGroupEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment OverviewGroupEventFragment.
     */
    public static OverviewArchivedGroupEventFragment newInstance() {
        OverviewArchivedGroupEventFragment fragment = new OverviewArchivedGroupEventFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_overview_archived_group_event, container, false);

        //get Views
        toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.toolbar_label_archived_group_event);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationContentDescription(R.string.toolbar_settings_navigation_description);
        toolbar.setNavigationOnClickListener((View v) -> {
            NavHostFragment.findNavController(this).navigateUp();
            toolbar.setNavigationIcon(null); //disbale icon in toolbar again
        });


        //bind recyclerView to DataModel
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_archived_groupEvents);
        GroupEventAdapter adapter = new GroupEventAdapter(getActivity());

        viewModel.getAllGroupEvents().observe(this, adapter::submitList);
//        viewModel.getAllNonArchivedGroupEvents().observe(this, adapter::submitList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //prevent short blinking of recyclerview, when data updates
        //see https://stackoverflow.com/questions/29331075/recyclerview-blinking-after-notifydatasetchanged
        recyclerView.getItemAnimator().setChangeDuration(0);

        //regsiter observer, that observes/ listens to click events on the list items
        adapter.registerObserver(new GroupEventObserver() {
            @Override
            public void onClickGroupEvent(GroupEvent groupEvent) {
//                OverviewGroupEventFragmentDirections.ActionOvrvwGeDestToDtlGeDest action = OverviewGroupEventFragmentDirections.actionOvrvwGeDestToDtlGeDest();
//                MainViewPagerFragmentDirections.ActionMvpDestToDtlGeDest action = MainViewPagerFragmentDirections.actionMvpDestToDtlGeDest();
                OverviewArchivedGroupEventFragmentDirections.ActionOvrwArchGeDestToEdtGeDest action = OverviewArchivedGroupEventFragmentDirections.actionOvrwArchGeDestToEdtGeDest();
                action.setGroupEventId(groupEvent.id);
                NavHostFragment.findNavController(getParentFragment()).navigate(action);
            }
            @Override
            public void onLongClickGroupEvent(GroupEvent groupEvent) {
            }
        });

        // Inflate the layout for this fragment
        return rootView;
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

}
