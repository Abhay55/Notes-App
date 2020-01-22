package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notes.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private Context ctx;
    public DbHandler(Context context) {
        super(context, "NOTETBL", null, 1);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("CREATE TABLE NOTETBL(ID INTEGER PRIMARY KEY, NOTE_TITLE TEXT, NOTE_DETAIL TEXT,DATE LONG);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

       // SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS NOTETBL;");
        onCreate(db);
    }

    //CRUD OPERATIONS

    //add note
    public void AddNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOTE_TITLE", note.getTitle());
        values.put("NOTE_DETAIL", note.getDetail());
        values.put("DATE", java.lang.System.currentTimeMillis());

        //Insert the row
        db.insert("NOTETBL", null, values);

        Log.d("Saved!!", "Saved to DB");

    }

    //Get a Note
    public Note getNote(int id){

        SQLiteDatabase db = this.getWritableDatabase();



        Cursor cursor = db.rawQuery("SELECT * FROM NOTETBL WHERE ID="+id,null);

        if (cursor != null)
            cursor.moveToFirst();


        Note note=new Note();
        note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID"))));
        note.setTitle(cursor.getString(cursor.getColumnIndex("NOTE_TITLE")));
        note.setDetail(cursor.getString(cursor.getColumnIndex("NOTE_DETAIL")));

        //convert timestamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex("DATE")))
                .getTime());

        note.setDateNoteAdded(formatedDate);
        return note;
    }

    //get all notes
    public List<Note> getAllNotes(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> allnotelist = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM NOTETBL",null);

        if (cursor.moveToFirst()) {
            do {
                Note note=new Note();
                note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID"))));
                note.setTitle(cursor.getString(cursor.getColumnIndex("NOTE_TITLE")));
                note.setDetail(cursor.getString(cursor.getColumnIndex("NOTE_DETAIL")));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex("DATE")))
                        .getTime());

                note.setDateNoteAdded(formatedDate);

                // Add to the allnotelist
                allnotelist.add(note);

            }while (cursor.moveToNext());
        }

        return allnotelist;
    }


    //Update Note
    public int updateNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOTE_TITLE", note.getTitle());
        values.put("NOTE_DETAIL", note.getDetail());
        values.put("DATE", java.lang.System.currentTimeMillis());//get system time


        //update row
        return db.update("NOTETBL", values, "ID", new String[] { String.valueOf(note.getId())} );
    }


    //Delete note
    public void deleteNote(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("NOTETBL", "ID",
                new String[] {String.valueOf(id)});

        db.close();

    }


    //Get Count
    public int getNotesCount(){
        String countQuery = "SELECT * FROM NOTETBL;";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
