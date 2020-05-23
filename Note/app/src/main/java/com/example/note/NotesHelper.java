package com.example.note;

import android.content.Context;

public class NotesHelper {
	private DatabaseHelper databaseHelper;
	private StorageHelper storageHelper;

	public NotesHelper(Context context) {
		databaseHelper = new DatabaseHelper(context);
		storageHelper = new StorageHelper();
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
