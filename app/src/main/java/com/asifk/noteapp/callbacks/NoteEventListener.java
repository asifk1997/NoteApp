package com.asifk.noteapp.callbacks;

import com.asifk.noteapp.model.Note;

public interface NoteEventListener {

    /**
     * call when note clicked
     * @param note: note iten
     */
    void onNoteClick(Note note);

    /**
     * call when long clicked
     * @param note: note item
     */
    void onNoteLongClick(Note note);

}
