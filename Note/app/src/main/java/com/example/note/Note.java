package com.example.note;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Note implements Serializable {
	private static int curId = 0;

	//database fields
	private int id, characterCount, wordCount, paragraphCount, readTime;
	private String reference, title;
	private Date dateCreated, dateModified;

	//other data
	private String preview;

	//TEMP CONSTRUCTOR FOR TESTING
	Note(String title, String preview) {
		this.title = title;
		this.preview = preview;
	}

	Note() {
		id = ++curId;
		reference = id + ".txt";
		//will be set on file creation in storage
		dateCreated = Calendar.getInstance(Locale.getDefault()).getTime();
		dateModified = dateCreated;
		//will be set on note modification
		title = "A new note";
		characterCount = 0;
		wordCount = 0;
		paragraphCount = 0;
		readTime = 0;
		preview = "Write something in your beautiful new note";
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

	//preview
	String getPreview() {
		return preview;
	}
	void setPreview(String preview) {
		this.preview = preview;
	}
}