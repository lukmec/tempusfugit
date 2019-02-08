package de.lumdev.tempusfugit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
//import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private NavController navCtrlr;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
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
            //tab unselected, means different tab is now selected
            navCtrlr.popBackStack(); //when overview is selected, user should not be able to use back button to move to previous dest (--> forcing to use tablayout again)
        }
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };
//    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find and store NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        this.navCtrlr = navHostFragment.getNavController();

        toolbar=findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab_main);
        tabLayout = findViewById(R.id.tabLayout_main);

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);



//        displayOverviewGroupEventFragment();

//        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        RecyclerView recyclerView = findViewById(R.id.recycler_groupEvents);
//        GroupEventAdapter adapter = new GroupEventAdapter(this);
//        viewModel.getAllGroupEvents().observe(this, adapter::submitList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    public void addGroupEvent(View v){
////        Intent intent = new Intent(getApplicationContext(), NewGroupEvent.class);
////        startActivity(intent);
//
////        removeDisplayedFragment();
//        fab.setImageResource(R.drawable.ic_check_black_24dp);
////        displayEditGroupEventFragment();
//        Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.action_overviewGroupEventFragment_to_editGroupEventFragment);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_aboutApp:
                return true;
            case R.id.menu_test1:
                return true;
            case R.id.menu_test2:
                return true;
            case R.id.menu_test3:
                return true;
            case R.id.menu_test4:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void displayOverviewGroupEventFragment(){
        OverviewGroupEventFragment overviewGroupEventFragment = OverviewGroupEventFragment.newInstance();
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        // Add the overviewGroupEventFragment.
        fragmentTransaction
                .add(R.id.fragment_container, overviewGroupEventFragment)
                .addToBackStack(null)
                .commit();
    }

    public void removeDisplayedFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if a fragment is already showing.
        Fragment alreadyDisplayedFragment = (Fragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (alreadyDisplayedFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(alreadyDisplayedFragment).commit();
        }
    }

    public void displayEditGroupEventFragment() {
        EditGroupEventFragment editGroupEventFragment = EditGroupEventFragment.newInstance();
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        // Add the editGroupEventFragment.
        fragmentTransaction
                .add(R.id.fragment_container, editGroupEventFragment)
                .addToBackStack(null)
                .commit();
    }
    */

}
