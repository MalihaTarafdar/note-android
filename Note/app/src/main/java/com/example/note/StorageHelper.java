package com.example.note;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class StorageHelper {
	private Context context;

	StorageHelper(Context context) {
		this.context = context;
	}

	//get content of note (not including title)
	String getNoteContent(Note note) {
		StringBuilder content = new StringBuilder();
		File file = new File(note.getReference());
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(context.openFileInput(file.getName())))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	//creates or overwrites a note in internal storage
	boolean createNote(Note note, String content) {
		File file = new File(note.getReference());
		try (OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(file.getName(),
				Context.MODE_PRIVATE))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//deletes a note from internal storage
	boolean deleteNote(Note note) {
		return context.deleteFile(note.getReference());
	}
}