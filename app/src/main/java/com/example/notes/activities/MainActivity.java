package com.example.notes.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.notes.R;
import com.example.notes.data.DbHandler;
import com.example.notes.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText get_title,get_note;
    Button sBtn;
    private DbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DbHandler(this);

        byPassActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();


                createPopupDialog();

            }
        });
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
                        && !get_title.getText().toString().isEmpty()){
                    saveNoteToDB(v);
                }


            }
        });
    }

    private void saveNoteToDB(View v){

        Note note=new Note();
        String newtitle = get_title.getText().toString();
        String newnote = get_note.getText().toString();

        note.setTitle(newtitle);
        note.setDetail(newnote);


        //Save to DB
        db.AddNote(note);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

      //  Log.d("item added id:",String.valueOf(db.getNotesCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start new activity
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);

    }

    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getNotesCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }
}
