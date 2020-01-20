package com.asifk.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.asifk.noteapp.adapters.NotesAdapter;
import com.asifk.noteapp.callbacks.NoteEventListener;
import com.asifk.noteapp.db.NotesDB;
import com.asifk.noteapp.db.NotesDao;
import com.asifk.noteapp.model.Note;
import com.asifk.noteapp.utils.NoteUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.asifk.noteapp.EditNotesActivity.NOTE_EXTRA_KEY;

public class MainActivity extends AppCompatActivity implements NoteEventListener {

    private static String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init recycler view
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // init fab button



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //todo add new note
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstance(this).notesDao();
    }

    private void loadNotes() {

        this.notes =new ArrayList<>();
        List<Note> list = dao.getNotes();

        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this,notes);

        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    private void onAddNewNote() {
       // todo start activity
        startActivity(new Intent(this,EditNotesActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();

    }

    @Override
    public void onNoteClick(Note note) {
        // note clicked
        //Toast.makeText(this,note.getId(),Toast.LENGTH_SHORT).show();

        Log.d(TAG,"onNoteClick: "+note.toString());
        Intent edit =new Intent(this,EditNotesActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY,note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(final Note note) {
        // note long clicked: delete ,share
        Log.d(TAG,"onNoteLongClick: "+note.getId());

        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo delete note from db
                        dao.deleteNote(note);
                        loadNotes();
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo share
                        Intent share =new Intent(Intent.ACTION_SEND);
                        String text =note.getNoteText()+"\n Created on :"+ NoteUtils.dateFromLong(note.getNoteDate())+" By :"+getString(R.string.app_name);
                        share.setType("text/plain");
                        Log.d(TAG,"onClick: "+text);
                        share.putExtra(Intent.EXTRA_TEXT,text);
                        startActivity(share);
                    }
                })
                .create()
                .show();
    }
}
