package com.example.note;

import java.io.Serializable;
import java.sql.Date;

class Note implements Serializable {
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

	Note(Date dateCreated) {
		this.dateCreated = dateCreated;
		id = -1; //will be set after note is created in database
		reference = id + ".txt"; //will be set after note is created in storage
		//will be set after note is modified
		title = "A new note";
		dateModified = dateCreated;
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
	Date getDateCreated() {
		return dateCreated;
	}
	void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	//date modified
	Date getDateModified() {
		return dateModified;
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