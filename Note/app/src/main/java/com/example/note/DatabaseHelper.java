package com.example.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements NoteDAO {

	private static final String DATABASE_NAME = "Note.db";

	private static final String TABLE_NOTE = "Note";

	private static final String FIELD_ID = "Id";
	private static final String FIELD_REFERENCE = "Reference"; //reference to file in storage
	private static final String FIELD_TITLE = "Title";
	private static final String FIELD_DATE_CREATED = "DateCreated";
	private static final String FIELD_DATE_MODIFIED = "DateModified";
	private static final String FIELD_CHARACTER_COUNT = "CharacterCount";
	private static final String FIELD_WORD_COUNT = "WordCount";
	private static final String FIELD_PARAGRAPH_COUNT = "ParagraphCount";
	private static final String FIELD_READ_TIME = "ReadTime"; //seconds

	DatabaseHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//generate Note table
		String createNoteTableStatement = "CREATE TABLE " + TABLE_NOTE + " (" +
				FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FIELD_REFERENCE + " TEXT NOT NULL, " +
				FIELD_TITLE + " TEXT, " +
				FIELD_DATE_CREATED + " DATE NOT NULL, " +
				FIELD_DATE_MODIFIED + " DATE NOT NULL, " +
				FIELD_CHARACTER_COUNT + " INT, " +
				FIELD_WORD_COUNT + " INT, " +
				FIELD_PARAGRAPH_COUNT + " INT, " +
				FIELD_READ_TIME + " INT" +
				");";
		db.execSQL(createNoteTableStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	//retrieves all the notes in the database
	@Override
	public List<Note> getAll() {
		return null;
	}

	//inserts a note into the database
	@Override
	public boolean insertNote(Note note) {
		return false;
	}

	//updates the fields of a note
	@Override
	public boolean updateNote(Note note) {
		return false;
	}

	//removes a note from the database
	@Override
	public boolean deleteNote(Note note) {
		return false;
	}
}