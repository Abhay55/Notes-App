package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.notes.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView note;
    private TextView dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = findViewById(R.id.titledet);
        note = findViewById(R.id.notedet);
        dateAdded = findViewById(R.id.dateAddedDet);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            title.setText(bundle.getString("title"));
            note.setText(bundle.getString("note"));
            dateAdded.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }
    }
}
