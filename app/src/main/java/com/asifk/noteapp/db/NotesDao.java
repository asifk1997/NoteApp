package com.asifk.noteapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.asifk.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if the note exists already replace it
    void insertNote(Note note);
    @Delete
    void deleteNote(Note note);
    @Update
    void updateNote(Note note);

    // list All notes From Database
    @Query("SELECT * FROM notes")
    List<Note> getNotes();
    @Query("SELECT * FROM notes WHERE id = :noteId") // get Note By Id
    Note getNoteById(int noteId);
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById (int noteId);
}
