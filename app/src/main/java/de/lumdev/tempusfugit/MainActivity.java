package de.lumdev.tempusfugit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
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

        toolbar=findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab_main);
        tabLayout = findViewById(R.id.tabLayout_main);

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //this is not needed anymore, cause navigation is handled by NavigationController (important: ids of menu-items and nav_graph destinations must be identical
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
