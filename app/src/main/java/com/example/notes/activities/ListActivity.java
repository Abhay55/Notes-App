package com.example.notes.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.notes.data.DbHandler;
import com.example.notes.model.Note;
import com.example.notes.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Note> Notelist;
    private List<Note> listitems;
    private DbHandler db;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText get_title, get_note;
    Button sBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DbHandler(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();


                createPopupDialog();
            }
        });

        db = new DbHandler(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Notelist = new ArrayList<>();
        listitems = new ArrayList<>();

        // Get items from database
        Notelist = db.getAllNotes();

        for (Note c : Notelist) {

            Note note = new Note();
            note.setTitle(c.getTitle());
            note.setDetail(c.getDetail());
            note.setId(c.getId());
            note.setDateNoteAdded("Added on:" + c.getDateNoteAdded());


            listitems.add(note);

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listitems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        get_title = (EditText) view.findViewById(R.id.getting_title);
        get_note = (EditText) view.findViewById(R.id.getting_note);
        sBtn = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //send to next activity or page

                if (!get_note.getText().toString().isEmpty()
                        && !get_title.getText().toString().isEmpty()) {
                    saveNoteToDB(v);
                    dialog.dismiss();

                    recyclerViewAdapter.notifyDataSetChanged();


                }


            }
        });
    }

    private void saveNoteToDB(View v) {

        Note note = new Note();
        String newtitle = get_title.getText().toString();
        String newnote = get_note.getText().toString();

        note.setTitle(newtitle);
        note.setDetail(newnote);


        //Save to DB
        db.AddNote(note);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        //  Log.d("item added id:",String.valueOf(db.getNotesCount()));


    }

}