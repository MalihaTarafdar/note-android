package com.example.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements NoteDAO {

	private static final String DATABASE_NAME = "Note.db";

	private static final String TABLE_NOTE = "Note";

	private static final String COLUMN_ID = "Id";
	private static final String COLUMN_REFERENCE = "Reference"; //reference to file in storage
	private static final String COLUMN_TITLE = "Title";
	private static final String COLUMN_DATE_CREATED = "DateCreated";
	private static final String COLUMN_DATE_MODIFIED = "DateModified";
	private static final String COLUMN_CHARACTER_COUNT = "CharacterCount";
	private static final String COLUMN_WORD_COUNT = "WordCount";
	private static final String COLUMN_PARAGRAPH_COUNT = "ParagraphCount";
	private static final String COLUMN_READ_TIME = "ReadTime"; //seconds

	DatabaseHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//generate Note table
		String createNoteTableStatement = "CREATE TABLE " + TABLE_NOTE + " (" +
				COLUMN_ID + " INTEGER PRIMARY KEY, " +
				COLUMN_REFERENCE + " TEXT, " +
				COLUMN_TITLE + " TEXT, " +
				COLUMN_DATE_CREATED + " DATETIME, " +
				COLUMN_DATE_MODIFIED + " DATETIME, " +
				COLUMN_CHARACTER_COUNT + " INT, " +
				COLUMN_WORD_COUNT + " INT, " +
				COLUMN_PARAGRAPH_COUNT + " INT, " +
				COLUMN_READ_TIME + " INT" +
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
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(COLUMN_ID, note.getId());
		cv.put(COLUMN_REFERENCE, note.getReference());
		cv.put(COLUMN_TITLE, note.getTitle());
		cv.put(COLUMN_DATE_CREATED, note.getDateCreated());
		cv.put(COLUMN_DATE_MODIFIED, note.getDateModified());
		cv.put(COLUMN_CHARACTER_COUNT, note.getCharacterCount());
		cv.put(COLUMN_WORD_COUNT, note.getWordCount());
		cv.put(COLUMN_PARAGRAPH_COUNT, note.getParagraphCount());
		cv.put(COLUMN_READ_TIME, note.getReadTime());

		long inserted = db.insert(TABLE_NOTE, null, cv);

		db.close();

		return inserted == 1;
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