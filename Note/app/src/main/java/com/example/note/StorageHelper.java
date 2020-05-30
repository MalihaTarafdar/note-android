package com.example.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class StorageHelper {
	static final int CREATE_FILE = 1;
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

	void exportNote(Note note, Uri uri, String ext) {
		switch (ext) {
			case "txt": exportAsTxt(note, uri);
				break;
			case "md": exportAsMd(note, uri);
				break;
		}
	}

	private void exportAsTxt(Note note, Uri uri) {
		try (FileOutputStream output = (FileOutputStream) context.getContentResolver().openOutputStream(uri)) {
			if (output == null) return;

			output.write((note.getTitle() + "\n\n").getBytes()); //write title to file
			output.write(note.getContent().getBytes()); //write content to file

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exportAsMd(Note note, Uri uri) {
		try (FileOutputStream output = (FileOutputStream) context.getContentResolver().openOutputStream(uri)) {
			if (output == null) return;

			output.write(("# " + note.getTitle() + "\n\n").getBytes()); //write title to file
			output.write(note.getContent().getBytes()); //write content to file

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}