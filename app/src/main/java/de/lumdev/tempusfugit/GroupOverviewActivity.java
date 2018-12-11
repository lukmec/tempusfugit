package de.lumdev.tempusfugit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GroupOverviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GroupOverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_overview);

        toolbar=findViewById(R.id.toolbar_group_overview);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(GroupOverviewViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recycler_groupEvents);
        GroupEventAdapter adapter = new GroupEventAdapter(this);
        viewModel.getAllGroupEvents().observe(this, adapter::submitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addGroupEvent(View v){
        Intent intent = new Intent(getApplicationContext(), NewGroupEvent.class);
        startActivity(intent);
    }


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
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
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

}
