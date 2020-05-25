package com.example.note;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

class StorageHelper {
	private Context context;

	StorageHelper(Context context) {
		this.context = context;
	}

	//creates a note in internal storage
	boolean createNote(Note note) {
		File file = new File(note.getReference());
		try (OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(file.getName(),
				Context.MODE_PRIVATE))) {
			writer.write(note.getContent());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//updates a note's content
	void updateNote(Note note) {

	}

	//deletes a note from internal storage
	boolean deleteNote(Note note) {
		return context.deleteFile(note.getReference());
	}
}