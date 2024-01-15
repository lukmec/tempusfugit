package de.lumdev.tempusfugit.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.lumdev.tempusfugit.MainPagerAdapter;
import de.lumdev.tempusfugit.R;

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
         mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(), getContext());
         //adding fragments (/ tabs) to viewpager (and tablayout)
         mainPagerAdapter.addFragment(new OverviewToDoEventFragment(), getString(R.string.tab_label_group_todo_overview));
         mainPagerAdapter.addFragment(new OverviewGroupEventFragment(), getString(R.string.tab_label_group_overview));
         mainPagerAdapter.addFragment(new OverviewEventFragment(), getString(R.string.tab_label_event_overview));
//         viewPager.setPageMargin(20); //for additional space between fragments
         viewPager.setAdapter(mainPagerAdapter);

         //init tab layout
         tabLayout = view.findViewById(R.id.tabLayout_main);
//         tabLayout.addOnTabSelectedListener(onTabSelectedListener);
         tabLayout.setupWithViewPager(viewPager);

         tabLayout.setTabTextColors(getResources().getColor(R.color.primaryLight200), getResources().getColor(R.color.onPrimary));
         tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.onPrimary));
         //set icon to tab
//         tabLayout.getTabAt(0).setIcon(R.drawable.ic_star_white_24dp);
//         tabLayout.getTabAt(0).setText(null);

         //set custom tab view (custom tab contains ticon and text)
//         tabLayout.getTabAt(0).setCustomView(mainPagerAdapter.getPageView(0));
//         tabLayout.getTabAt(1).setCustomView(mainPagerAdapter.getPageView(1));
//         // Iterate over all tabs and set the custom view
//         for (int i = 0; i < tabLayout.getTabCount(); i++) {
//             TabLayout.Tab tab = tabLayout.getTabAt(i);
//             tab.setCustomView(mainPagerAdapter.getPageView(i));
//         }

         //do action when scrolling pages (see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener)
         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             }
             @Override
             public void onPageSelected(int position) {
//                 Log.d("--> position", String.valueOf(position));
                 MainPagerAdapter adapter = (MainPagerAdapter) viewPager.getAdapter();
                 adapter.setNewDoneStatesInOvrvwToDoEventFragment(0);
                 adapter.setNewDoneStatesInOvrvwEventFragment(2);

             }

             @Override
             public void onPageScrollStateChanged(int state) {
//                 //if (state == ViewPager.SCROLL_STATE_IDLE){}
//                 if (state == ViewPager.SCROLL_STATE_DRAGGING){
//                     MainPagerAdapter adapter = (MainPagerAdapter) viewPager.getAdapter();
//                     int positionOfOvrwFragm = 2;
//                     adapter.setNewDoneStatesInOvrvwEventFragment(positionOfOvrwFragm);
//                 }
//                 //if (state == ViewPager.SCROLL_STATE_SETTLING){}
             }
         });
     }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
    }

}
