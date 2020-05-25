package com.example.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

	private Context context;

	DatabaseHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.context = context;
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

	//retrieves a single note from the database from the id
	@Override
	public Note getNoteById(int id) {
		Note note = new Note(context);
		note.setId(id);

		SQLiteDatabase db = this.getReadableDatabase();
		String getByIdQuery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + COLUMN_ID + " = " + id;

		Cursor cursor = db.rawQuery(getByIdQuery, null);
		if (cursor.moveToFirst()) {
			note.setReference(cursor.getString(1));
			note.setTitle(cursor.getString(2));

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			try {
				note.setDateCreated(dateFormat.parse(cursor.getString(3)));
				note.setDateModified(dateFormat.parse(cursor.getString(4)));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			note.setCharacterCount(cursor.getInt(5));
			note.setWordCount(cursor.getInt(6));
			note.setParagraphCount(cursor.getInt(7));
			note.setReadTime(cursor.getInt(8));
		}

		cursor.close();
		db.close();

		return note;
	}

	//retrieves all the notes in the database
	@Override
	public List<Note> getAll() {
		List<Note> notes = new ArrayList<>();

		SQLiteDatabase db = this.getReadableDatabase();
		String getAllQuery = "SELECT * FROM " + TABLE_NOTE;

		Cursor cursor = db.rawQuery(getAllQuery, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				notes.add(getNoteById(id));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return notes;
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
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_NOTE + " WHERE " + COLUMN_ID + " = " + note.getId();

		Cursor cursor = db.rawQuery(deleteQuery, null);
		boolean deleted = cursor.moveToFirst();

		cursor.close();

		return deleted;
	}
}