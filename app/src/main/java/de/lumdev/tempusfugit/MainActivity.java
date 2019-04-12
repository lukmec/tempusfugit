package de.lumdev.tempusfugit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
//import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private NavController navCtrlr;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private MainPagerAdapter mainPagerAdapter;
    private TabLayout tabLayout;
    private TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //set selected tab to bold
            TextView textView = (TextView) tab.getCustomView();
            textView.setTypeface(null, Typeface.BOLD);

            switch (tab.getPosition()){
                case 0:
                    //Group Overview Selected
                    navCtrlr.navigate(R.id.ovrvw_ge_dest);
                    break;
                case 1:
                    //Event Overview Selected
                    navCtrlr.navigate(R.id.ovrvw_e_dest);
                    break;
            }
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //set unselected tab back to normal
            TextView textView = (TextView) tab.getCustomView();
            textView.setTypeface(null, Typeface.NORMAL);
            //tab unselected, means different tab is now selected
            navCtrlr.popBackStack(); //when overview is selected, user should not be able to use back button to move to previous dest (--> forcing to use tablayout again)
        }
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find and store NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        this.navCtrlr = navHostFragment.getNavController();

//        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.setNavController(this.navCtrlr);

//        //init viewPager
//        viewPager = findViewById(R.id.main_view_pager);
//        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(mainPagerAdapter);
////        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
////        tabLayout.setupWithViewPager(viewPager);

        toolbar=findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
//        fab = findViewById(R.id.fab_main);

//        tabLayout = findViewById(R.id.tabLayout_main);
//        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        //code to make font changes of tablayout work
        //see https://stackoverflow.com/questions/32031437/how-do-i-change-the-text-style-of-a-selected-tab-when-using-tablayout/40903654
//            for (int i = 0; i < tabLayout.getTabCount(); i++) {
//
//                TabLayout.Tab tab = tabLayout.getTabAt(i);
//                if (tab != null) {
//
//                    TextView tabTextView = new TextView(this);
//                    tab.setCustomView(tabTextView);
//
//                    tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//
//                    tabTextView.setText(tab.getText());
//                    tabTextView.setTextColor(getResources().getColor(R.color.colorAccent));
//                    tabTextView.setScaleX(1.25f);
//                    tabTextView.setScaleY(1.25f);
//
//                    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//                    // First tab is the selected tab, so if i==0 then set BOLD typeface
//                    if (i == 0) {
//                        tabTextView.setTypeface(null, Typeface.BOLD);
//                    }
//
//                }
//
//            }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //this is not needed anymore, cause navigation is handled by NavigationController (important: ids of menu-items and nav_graph_v1 destinations must be identical
        //see https://developer.android.com/topic/libraries/architecture/navigation/navigation-ui#Tie-navdrawer
//            switch (item.getItemId()){
//                case R.id.settings_dest:
//                    Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
//                    startActivity(settingsIntent);
//                    return true;
//                case R.id.abt_app_dest:
//                    return true;
//            }
//            return super.onOptionsItemSelected(item);

        return NavigationUI.onNavDestinationSelected(item, this.navCtrlr) || super.onOptionsItemSelected(item);
    }

}
