package com.example.note;

import android.content.Context;

public class NotesHelper {
	private StorageHelper storageHelper;
	private DatabaseHelper databaseHelper;

	public NotesHelper(Context context) {
		storageHelper = new StorageHelper(context);
		databaseHelper = new DatabaseHelper(context);
	}

	public void createNote(Note note) {
		storageHelper.createNote(note);
		databaseHelper.insertNote(note);
	}

	public void updateNote(Note note) {
		storageHelper.updateNote(note);
		databaseHelper.updateNote(note);
	}

	public void deleteNote(Note note) {
		storageHelper.deleteNote(note);
		databaseHelper.deleteNote(note);
	}
}
