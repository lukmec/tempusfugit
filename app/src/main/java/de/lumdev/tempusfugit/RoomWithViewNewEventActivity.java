package de.lumdev.tempusfugit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RoomWithViewNewEventActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText etName;
    private EditText etDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_with_view_new_event);
        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(etName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = etName.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, name);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }}
