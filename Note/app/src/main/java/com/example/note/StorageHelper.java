package com.example.note;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

class StorageHelper {
	private final String rootDir = "Note";

	StorageHelper(Context context) {
		//create test file in root dir in internal storage
		String test = "this is a test text\nhello world";
		File file = new File(context.getDir(rootDir, Context.MODE_PRIVATE), "test.txt");

		//metadata
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			try {
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
				Log.d("TAG", attr.creationTime().toString());
				Log.d("TAG", attr.lastModifiedTime().toString()); //GMT
				Log.d("TAG", attr.size() + "");
				Log.d("TAG", file.lastModified() + ""); //epoch time
				Log.d("TAG", file.length() + "");
			} catch (IOException e) {
				Log.d("TAG", "IOException");
			}
		}

		try {
			OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(file.getName(), Context.MODE_PRIVATE));
			writer.write(test);
			writer.close();

			File[] files = context.getDir(rootDir, Context.MODE_PRIVATE).listFiles();
			for (File f : files) {
				Log.d("TAG", f.getAbsolutePath());
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(file.getName())));
			String line;
			while ((line = reader.readLine()) != null) {
				Log.d("TAG", line);
			}
			reader.close();
		} catch (IOException e) {
			Log.d("TAG", "error");
		}
	}

	//creates a note in the app's filesystem
	void createNote(Note note) {

	}

	//updates a note's content
	void updateNote(Note note) {

	}

	//deletes a note from the app's filesystem
	void deleteNote(Note note) {

	}
}