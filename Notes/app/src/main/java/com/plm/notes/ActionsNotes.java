package com.plm.notes;

import com.plm.notes.exceptions.ContentCarsException;
import com.plm.notes.exceptions.DuplicateNoteException;
import com.plm.notes.exceptions.NoteNotFoundException;
import com.plm.notes.exceptions.NotesException;
import com.plm.notes.exceptions.TitleCarsException;
import com.plm.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class ActionsNotes implements IActionsNotes{
    private static List bdNotes;

    @Override
    public void persistNote(Note note) throws DuplicateNoteException, TitleCarsException, ContentCarsException, NotesException {

    }

    @Override
    public void deleteNote(Note note) throws NoteNotFoundException, NotesException {

    }

    @Override
    public List<Note> filterNotes(String texto) throws NotesException {
        return null;
    }

    @Override
    public List<Note> getNotes() throws NotesException {
        return null;
    }

    @Override
    public Note getNote(int index) throws Exception {
        return null;
    }


    static{
        bdNotes = new ArrayList();

        bdNotes.add(new Note("compra","alubias,casera","10/10/2021"));
        bdNotes.add(new Note("series","netflix,hbo,disney","14/11/2021"));
        bdNotes.add(new Note("políticos","Suarez, González, Rivera","13/11/2021"));
        bdNotes.add(new Note("cuidado","hay un perro que muerde!!!","01/10/2020"));
    }
}
