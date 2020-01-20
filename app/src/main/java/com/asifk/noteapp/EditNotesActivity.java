package com.asifk.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.asifk.noteapp.db.NotesDB;
import com.asifk.noteapp.db.NotesDao;
import com.asifk.noteapp.model.Note;

import java.util.Date;

public class EditNotesActivity extends AppCompatActivity {

    private EditText inputNote;
    private NotesDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        inputNote = findViewById(R.id.input_note);
        dao = NotesDB.getInstance(this).notesDao();

        if (getIntent().getExtras()!=null)
        {
            Log.d("AK","got extras");
            int id= getIntent().getExtras().getInt(NOTE_EXTRA_KEY,0);
            Log.d("AK","got extras id"+id);
            temp = dao.getNoteById(id);

            inputNote.setText(temp.getNoteText());
        }
        else
        {
            temp=new Note();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        // todo save note
        String text = inputNote.getText().toString();
        if (!text.isEmpty())
        {
            long date =new Date().getTime();
           // Note note = new Note(text,date); // Create new Note

            temp.setNoteDate(date);
            temp.setNoteText(text);
            Log.e("AK","tempid "+temp.getId());
            if (temp.getId()== 0)
            {
                dao.insertNote(temp);

            }
            else{
                dao.updateNote(temp);
            }



            finish();
        }
    }
}
