package com.example.notes.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.activities.DetailsActivity;
import com.example.notes.data.DbHandler;
import com.example.notes.model.Note;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Note> NoteItem;
    private DbHandler db;
    private AlertDialog.Builder alertDialogueBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<Note> noteItem) {
        this.context = context;
        this.NoteItem = noteItem;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view,context);

    }

    @Override
    public void onBindViewHolder( RecyclerViewAdapter.ViewHolder holder, int position) {

        Note note=NoteItem.get(position);

        holder.set_title.setText(note.getTitle());
        holder.set_note.setText(note.getDetail());
        holder.set_date.setText(note.getDateNoteAdded());
    }

    @Override
    public int getItemCount() {
        return NoteItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView set_title;
        public TextView set_note;
        public TextView set_date;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder( View view,Context ctx) {
            super(view);

            context=ctx;
            set_title = (TextView) view.findViewById(R.id.setting_title);
            set_note = (TextView) view.findViewById(R.id.setting_note);
            set_date = (TextView) view.findViewById(R.id.setting_date);

            editButton = (Button) view.findViewById(R.id.editbtn);
            deleteButton = (Button) view.findViewById(R.id.deletebtn);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next activity--details activity

                    int position=getAdapterPosition();

                    Note note=NoteItem.get(position);
                    Intent intent=new Intent(context, DetailsActivity.class);
                    intent.putExtra("title",note.getTitle());
                    intent.putExtra("note",note.getDetail());
                    intent.putExtra("id",note.getId());
                    intent.putExtra("date",note.getDateNoteAdded());

                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editbtn:
                    int position =getAdapterPosition();
                    Note note=NoteItem.get(position);
                    editNote(note);

                    break;

                case R.id.deletebtn:
                     position =getAdapterPosition();
                     note=NoteItem.get(position);
                    deleteNote(note.getId());

                    break;
            }


        }

        public void deleteNote(final int id){

            alertDialogueBuilder=new AlertDialog.Builder(context);

            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confirmation_dialogue,null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogueBuilder.setView(view);
            alertDialog = alertDialogueBuilder.create();
            alertDialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DbHandler db = new DbHandler(context);
                    //delete item
                    db.deleteNote(id);
                    NoteItem.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();


                }
            });

        }

        public void editNote(final Note note) {

            alertDialogueBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText up_title = (EditText) view.findViewById(R.id.getting_title);
            final EditText up_note = (EditText) view.findViewById(R.id.getting_note);
            final TextView up_tv = (TextView) view.findViewById(R.id.tile);

            up_tv.setText("Edited note");
            Button saveButton = (Button) view.findViewById(R.id.saveButton);


            alertDialogueBuilder.setView(view);
            alertDialog = alertDialogueBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DbHandler db = new DbHandler(context);

                    //Update item
                    note.setTitle(up_title.getText().toString());
                    note.setDetail(up_note.getText().toString());

                    if (!up_title.getText().toString().isEmpty()
                            && !up_note.getText().toString().isEmpty()) {
                        db.updateNote(note);
                        notifyItemChanged(getAdapterPosition(),note);
                    }else {
                        Snackbar.make(view, "Add title and note", Snackbar.LENGTH_LONG).show();
                    }

                    alertDialog.dismiss();

                }
            });

        }
    }
}
