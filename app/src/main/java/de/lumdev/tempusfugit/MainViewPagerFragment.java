package de.lumdev.tempusfugit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class MainViewPagerFragment extends Fragment {

    private ViewPager viewPager;
    private MainPagerAdapter mainPagerAdapter;
    private TabLayout tabLayout;

    public MainViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_view_pager, container, false);
    }

     @Override
     public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);

         //init viewPager
         viewPager = view.findViewById(R.id.main_view_pager);
         mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
         //adding fragments (/ tabs) to viewpager (and tablayout)
         mainPagerAdapter.addFragment(new OverviewGroupEventFragment(), getString(R.string.tab_label_group_overview));
         mainPagerAdapter.addFragment(new OverviewEventFragment(), getString(R.string.tab_label_event_overview));
//         viewPager.setPageMargin(20); //for additional space between fragments
         viewPager.setAdapter(mainPagerAdapter);

         //init tab layout
         tabLayout = view.findViewById(R.id.tabLayout_main);
//         tabLayout.addOnTabSelectedListener(onTabSelectedListener);
         tabLayout.setupWithViewPager(viewPager);
     }

}
