package com.plm.notes;

import com.plm.notes.exceptions.ContentCarsException;
import com.plm.notes.exceptions.DuplicateNoteException;
import com.plm.notes.exceptions.NoteNotFoundException;
import com.plm.notes.exceptions.NotesException;
import com.plm.notes.exceptions.TitleCarsException;
import com.plm.notes.model.Note;

import java.util.List;

public interface IActionsNotes {
    public void persistNote(Note note) throws DuplicateNoteException, TitleCarsException, ContentCarsException, NotesException;
    public void deleteNote(Note note) throws NoteNotFoundException, NotesException;
    public List<Note> filterNotes(String texto) throws NotesException;
    public List<Note> getNotes() throws NotesException;
    public Note getNote(int index) throws Exception;
}
