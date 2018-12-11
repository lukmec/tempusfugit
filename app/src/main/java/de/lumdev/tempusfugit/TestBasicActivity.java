package de.lumdev.tempusfugit;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TestBasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.test_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //---- Beispiel für Shared Preferences
//        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);
//        if(sP.getBoolean("notifications_new_message", false)){
//            tV.setText("1 - true - ");
//        }

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
