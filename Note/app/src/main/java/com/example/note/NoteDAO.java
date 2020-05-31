package com.example.note;

import java.util.List;

interface NoteDAO {
	Note getNoteById(int id);
	List<Note> getAll();
	boolean insertNote(Note note);
	boolean updateNote(Note note);
	boolean deleteNote(Note note);
}
