package com.example.note;

import java.util.List;

public interface NoteDAO {
	List<Note> getAll();
	boolean insertNote(Note note);
	boolean updateNote(Note note);
	boolean deleteNote(Note note);
}
