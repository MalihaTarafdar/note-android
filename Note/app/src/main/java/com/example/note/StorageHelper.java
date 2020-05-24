package com.example.note;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

class StorageHelper {
	private final String rootDir = "Note";
	private Context context;

	StorageHelper(Context context) {
		this.context = context;
	}

	//creates a note in the app's filesystem
	void createNote(Note note) {
		File file = new File(context.getDir(rootDir, Context.MODE_PRIVATE), note.getReference());
		try (OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(file.getName(), Context.MODE_PRIVATE))) {
			writer.write(note.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//updates a note's content
	void updateNote(Note note) {

	}

	//deletes a note from the app's filesystem
	void deleteNote(Note note) {

	}
}