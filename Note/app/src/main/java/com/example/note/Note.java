package com.example.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Note {
	//id count so no ids are repeated
	static int curId = 0;

	//database fields
	private int id, characterCount, wordCount, paragraphCount, readTime;
	private String reference, title;
	private Date dateCreated, dateModified;

	private Context context;
	private StorageHelper storageHelper;
	private DatabaseHelper databaseHelper;

	Note(Context context) {
		this.context = context;
		//set note data
		title = "";
		dateCreated = Calendar.getInstance(Locale.getDefault()).getTime();
		dateModified = dateCreated;
		characterCount = 0;
		wordCount = 0;
		paragraphCount = 0;
		readTime = 0;

		//set helpers
		storageHelper = new StorageHelper(context);
		databaseHelper = new DatabaseHelper(context);
	}

	String getContent() {
		return storageHelper.getNoteContent(this);
	}

	void create() {
		id = ++curId;
		//save id
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.id_pref), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(context.getString(R.string.id_key), id);
		editor.apply();

		reference = id + ".txt";
		storageHelper.createNote(this, "");
		databaseHelper.insertNote(this);
	}

	void save(String title, String content, Date dateModified) {
		this.title = title;
		this.dateModified = dateModified;

		//character count
		characterCount = content.length();

		//word count
		int wordCount = 0;
		String[] words = content.split("\\s+");
		for (String word : words) {
			if (!word.matches("[\\p{Punct}]+")) wordCount++;
		}
		this.wordCount = wordCount;

		//paragraph count
		int paragraphCount = 0;
		String[] paragraphs = content.split("\\n+");
		for (String paragraph : paragraphs) {
			if (!paragraph.matches("[\\s\\p{Punct}]+")) paragraphCount++;
		}
		this.paragraphCount = paragraphCount;

		//read time based on average reading speed of 200wpm and 5 characters per word
		readTime = (int)(characterCount / 5.0 / 200.0 * 60);

		storageHelper.createNote(this, content);
		databaseHelper.updateNote(this);
	}

	void delete() {
		storageHelper.deleteNote(this);
		databaseHelper.deleteNote(this);
	}

	void export(Uri uri, String ext) {
		storageHelper.exportNote(this, uri, ext);
	}

	//ID
	int getId() {
		return id;
	}
	void setId(int id) {
		this.id = id;
		reference = id + ".txt";
	}

	//reference
	String getReference() {
		return reference;
	}
	void setReference(String reference) {
		this.reference = reference;
	}

	//title
	String getTitle() {
		return title;
	}
	void setTitle(String title) {
		this.title = title;
	}

	//date created
	String getDateCreated() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(dateCreated);
	}
	void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	//date modified
	String getDateModified() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(dateModified);
	}
	void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	//character count
	int getCharacterCount() {
		return characterCount;
	}
	void setCharacterCount(int characterCount) {
		this.characterCount = characterCount;
	}

	//word count
	int getWordCount() {
		return wordCount;
	}
	void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	//paragraph count
	int getParagraphCount() {
		return paragraphCount;
	}
	void setParagraphCount(int paragraphCount) {
		this.paragraphCount = paragraphCount;
	}

	//read time
	int getReadTime() {
		return readTime;
	}
	void setReadTime(int readTime) {
		this.readTime = readTime;
	}

	@NonNull
	@Override
	public String toString() {
		return id + "";
	}
}