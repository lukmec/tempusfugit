package de.lumdev.tempusfugit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.DateTimeTypeConverter;
import de.lumdev.tempusfugit.data.GroupEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.provider.CalendarContract.Calendars;
import android.widget.Toast;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tV_hello_world;

    private MainActivityViewModel viewModel;

    public static final int NEW_EVENT_ACTIVITY_REQUEST_CODE = 1;

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV_hello_world = findViewById(R.id.tV_test_msg);


        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        GroupEventListAdapter2 adapter2 = new GroupEventListAdapter2(this);
        viewModel.getAllGroupEvents().observe(this, adapter2::submitList);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        viewModel.init(1);
//        viewModel.getEvent().observe(this, event -> {
//            // Update UI.
//            tV_hello_world.setText(event.getTitle());
//        });
//        viewModel.getMyTestString().observe(this, testString -> {
//            // Update UI.
//            tV_hello_world.setText(testString);
//        });


//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final GroupEventListAdapter adapter = new GroupEventListAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        viewModel.getAllGroupEvents().observe(this, new Observer<List<GroupEvent>>() {
//            @Override
//            public void onChanged(@Nullable final List<GroupEvent> gEvents) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setGroupEvents(gEvents);
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EVENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            GroupEvent gEvent = new GroupEvent(data.getStringExtra(RoomWithViewNewEventActivity.EXTRA_REPLY), "default description");
            viewModel.insertGroupEvent(gEvent);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Event not saved in DB.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onReadAccount(View v) {
        AccountManager am = AccountManager.get(this); // "this" references the current Context
        Account[] accounts = am.getAccountsByType("com.google");
        tV_hello_world.setText(accounts[0].name + " (Type: "+accounts[0].type+")");
    }

    public void onReadCalendar(View v) {

        Intent intent = new Intent(MainActivity.this, TestBasicActivity.class);
        startActivity(intent);

//        // Run query
//        Cursor cur = null;
//        ContentResolver cr = getContentResolver();
//        Uri uri = Calendars.CONTENT_URI;
//        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] selectionArgs = new String[] {"lu.mechura@gmail.com", "com.google",
//                "lu.mechura@gmail.com"};
//        // Submit the query and get a Cursor object back.
//        try {
//            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
//        }catch (SecurityException e){
//            Log.d("Exception thrown", "Calender Read Permission not set.");
//        }
//
//        // Use the cursor to step through the returned records
//        while (cur.moveToNext()) {
//            long calID = 0;
//            String displayName = null;
//            String accountName = null;
//            String ownerName = null;
//
//            // Get the field values
//            calID = cur.getLong(PROJECTION_ID_INDEX);
//            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
//
//            // Do something with the values...
//            Toast.makeText(getApplicationContext(), calID+" - Name: "+displayName, Toast.LENGTH_SHORT).show();
//
//        }
//        cur.close();
    }

    public void onTest3(View v){

        //viewModel.getMyTestString().setValue("Zeit: "+System.currentTimeMillis());

//        OffsetDateTime now = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
//        Duration d = Duration.ZERO.plusMinutes(20);
//        toast("0: "+d.toString());
//        try {
//            String nowString = DateTimeTypeConverter.fromOffsetDateTime(now);
//            toast("1: " + nowString);
//            try{
//                OffsetDateTime date = DateTimeTypeConverter.toOffsetDateTime(nowString);
//                toast("2: "+date.toString());
//            }catch (Exception e){
//                toast("2: Error while conversion String -> Date");
//            }
//        }catch(Exception e){
//            toast("1: Error while conversion Date -> String");
//        }

        Intent intent = new Intent(MainActivity.this, RoomWithViewNewEventActivity.class);
        startActivityForResult(intent, NEW_EVENT_ACTIVITY_REQUEST_CODE);

//        Cursor cursor;
//        if (android.os.Build.VERSION.SDK_INT <= 7) {
//            cursor = getContentResolver().query(Uri.parse("content://calendar/calendars"), new String[] { "_id", "displayName" }, null,
//                    null, null);
//
//        }
//
//        else if (android.os.Build.VERSION.SDK_INT <= 14) {
//            cursor = getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"),
//                    new String[] { "_id", "displayName" }, null, null, null);
//
//        }
//
//        else {
//            cursor = getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"),
//                    new String[] { "_id", "calendar_displayName" }, null, null, null);
//
//        }
//
//        // Get calendars name
//        Log.i("TempusFugit","Cursor count " + cursor.getCount());
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            String[] calendarNames = new String[cursor.getCount()];
//            // Get calendars id
//            int calendarIds[] = new int[cursor.getCount()];
//            for (int i = 0; i < cursor.getCount(); i++) {
//                calendarIds[i] = cursor.getInt(0);
//                calendarNames[i] = cursor.getString(1);
//                Log.i("TempusFugit","Calendar Name : " + calendarNames[i]);
//                Toast.makeText(getApplicationContext(), calendarNames[i], Toast.LENGTH_SHORT).show();
//                cursor.moveToNext();
//            }
//        } else {
//            Log.e("TempusFugit","No calendar found in the device");
//        }
    }

    public void toast(String textToToast){
        Toast.makeText(getApplicationContext(), textToToast, Toast.LENGTH_SHORT).show();
    }
}
