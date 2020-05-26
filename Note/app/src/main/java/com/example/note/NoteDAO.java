package com.example.note;

import java.util.List;

interface NoteDAO {
	//Sort options
	enum SortOption {
		TITLE, DATE_CREATED, DATE_MODIFIED, CHARACTER_COUNT, WORD_COUNT, PARAGRAPH_COUNT, READ_TIME, ALL
	}

	Note getNoteById(int id);
	List<Note> getAll(DatabaseHelper.SortOption sortOption);
	boolean insertNote(Note note);
	boolean updateNote(Note note);
	boolean deleteNote(Note note);
}
