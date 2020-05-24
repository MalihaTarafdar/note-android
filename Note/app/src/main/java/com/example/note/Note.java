package com.example.note;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Note {
	private static int curId = 0;

	//database fields
	private int id, characterCount, wordCount, paragraphCount, readTime;
	private String reference, title;
	private Date dateCreated, dateModified;

	//other data
	private String content, previewContent, previewTitle;

	private StorageHelper storageHelper;
	private DatabaseHelper databaseHelper;

	Note(Context context) {
		//set note data
		id = ++curId;
		reference = id + ".txt";
		title = "";
		dateCreated = Calendar.getInstance(Locale.getDefault()).getTime();
		dateModified = dateCreated;
		characterCount = 0;
		wordCount = 0;
		paragraphCount = 0;
		readTime = 0;
		content = "";
		previewTitle = "A new note";
		previewContent = "Write something in your beautiful new note";

		//create note in storage
		storageHelper = new StorageHelper(context);
		storageHelper.createNote(this);

		//create note to database
		databaseHelper = new DatabaseHelper(context);
		databaseHelper.insertNote(this);
	}

	void update(String title, String content, Date dateModified) {
		setTitle(title);
		setContent(content);
		setDateModified(dateModified);
		if (title.matches("\\S")) setPreviewTitle(title);
		if (content.matches("\\S")) setPreviewContent(content);
		//TODO: calculate and set note length measurements
		storageHelper.updateNote(this);
		databaseHelper.updateNote(this);
	}

	void delete() {
		storageHelper.deleteNote(this);
		databaseHelper.deleteNote(this);
	}

	//ID
	int getId() {
		return id;
	}
	void setId(int id) {
		this.id = id;
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

	//content
	String getContent() {
		return content;
	}
	void setContent(String content) {
		this.content = content;
	}

	//preview
	String getPreviewContent() {
		return previewContent;
	}
	void setPreviewContent(String previewContent) {
		this.previewContent = previewContent;
	}

	String getPreviewTitle() {
		return previewTitle;
	}
	void setPreviewTitle(String previewTitle) {
		this.previewTitle = previewTitle;
	}
}