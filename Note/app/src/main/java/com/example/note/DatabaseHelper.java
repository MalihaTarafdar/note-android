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

class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Note.db";

	private static final String TABLE_NOTE = "Note";

	//database columns
	private static final String COL_ID = "Id";
	private static final String COL_REFERENCE = "Reference"; //reference to file in storage
	private static final String COL_TITLE = "Title";
	private static final String COL_DATE_CREATED = "DateCreated";
	private static final String COL_DATE_MODIFIED = "DateModified";
	private static final String COL_CHARACTER_COUNT = "CharacterCount";
	private static final String COL_WORD_COUNT = "WordCount";
	private static final String COL_PARAGRAPH_COUNT = "ParagraphCount";
	private static final String COL_READ_TIME = "ReadTime"; //seconds

	private Context context;

	DatabaseHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//generate Note table
		String createNoteTableStatement = "CREATE TABLE " + TABLE_NOTE + " (" +
				COL_ID + " INTEGER PRIMARY KEY, " +
				COL_REFERENCE + " TEXT, " +
				COL_TITLE + " TEXT, " +
				COL_DATE_CREATED + " DATETIME, " +
				COL_DATE_MODIFIED + " DATETIME, " +
				COL_CHARACTER_COUNT + " INT, " +
				COL_WORD_COUNT + " INT, " +
				COL_PARAGRAPH_COUNT + " INT, " +
				COL_READ_TIME + " INT" +
				");";
		db.execSQL(createNoteTableStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	//retrieves a single note from the database from the id
	Note getNoteById(int id) {
		Note note = new Note(context);
		note.setId(id);

		SQLiteDatabase db = this.getReadableDatabase();
		String getByIdQuery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + COL_ID + " = " + id;

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

	//retrieves all the notes in the database with provided sort and filter options
	List<Note> getAll(List<SortData> sortByList, List<FilterData> filterByList) {
		List<Note> notes = new ArrayList<>();

		SQLiteDatabase db = this.getReadableDatabase();
		StringBuilder getQuery = new StringBuilder("SELECT * FROM " + TABLE_NOTE);

		//add filtering to query
		if (filterByList.size() > 0) {
			getQuery.append(" WHERE ");
			for (FilterData fd : filterByList) {
				getQuery.append(fd.getCol());
				if (fd.hasLowerBound() && fd.hasUpperBound()) {
					getQuery.append(" BETWEEN ").append(fd.getLowerBound()).append(" AND ").append(fd.getUpperBound());
				} else if (fd.hasLowerBound()) {
					getQuery.append(" >= ").append(fd.getLowerBound());
				} else if (fd.hasUpperBound()) {
					getQuery.append(" <= ").append(fd.getUpperBound());
				}
				getQuery.append(" AND ");
			}
			getQuery.setLength(getQuery.length() - 5);
		}

		//add sorting to query
		if (sortByList.size() > 0) {
			getQuery.append(" ORDER BY ");
			for (SortData sd : sortByList) {
				getQuery.append(sd.getCol()).append((sd.isAscending()) ? " ASC, " : " DESC, ");
			}
			getQuery.setLength(getQuery.length() - 2);
		}

//		Toast.makeText(context, getQuery.toString(), Toast.LENGTH_SHORT).show();

		Cursor cursor = db.rawQuery(getQuery.toString(), null);
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
	boolean insertNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = getNoteCV(note);
		long inserted = db.insert(TABLE_NOTE, null, cv);
		db.close();
		return inserted == 1;
	}

	//updates the fields of a note
	boolean updateNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = getNoteCV(note);
		long updated = db.update(TABLE_NOTE, cv, COL_ID + " = " + note.getId(), null);
		db.close();
		return updated == 1;
	}

	//get the ContentValues of a note
	private ContentValues getNoteCV(Note note) {
		ContentValues cv = new ContentValues();

		cv.put(COL_ID, note.getId());
		cv.put(COL_REFERENCE, note.getReference());
		cv.put(COL_TITLE, note.getTitle());
		cv.put(COL_DATE_CREATED, note.getDateCreated());
		cv.put(COL_DATE_MODIFIED, note.getDateModified());
		cv.put(COL_CHARACTER_COUNT, note.getCharacterCount());
		cv.put(COL_WORD_COUNT, note.getWordCount());
		cv.put(COL_PARAGRAPH_COUNT, note.getParagraphCount());
		cv.put(COL_READ_TIME, note.getReadTime());

		return cv;
	}

	//removes a note from the database
	boolean deleteNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_NOTE + " WHERE " + COL_ID + " = " + note.getId();

		Cursor cursor = db.rawQuery(deleteQuery, null);
		boolean deleted = cursor.moveToFirst();

		cursor.close();

		return deleted;
	}

	static class SortData {
		private String col;
		private boolean ascending;
		SortData(String col, boolean ascending) {
			this.col = col;
			this.ascending = ascending;
		}
		String getCol() {
			return col;
		}
		void setCol(String col) {
			this.col = col;
		}
		boolean isAscending() {
			return ascending;
		}
		void setAscending(boolean ascending) {
			this.ascending = ascending;
		}
	}

	static class FilterData {
		private String col, lowerBound, upperBound;
		FilterData(String col, String lowerBound, String upperBound) {
			this.col = col;
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}
		boolean hasLowerBound() {
			return lowerBound != null && lowerBound.replaceAll("'", "").length() != 0;
		}
		boolean hasUpperBound() {
			return upperBound != null && upperBound.replaceAll("'", "").length() != 0;
		}
		String getCol() {
			return col;
		}
		void setCol(String col) {
			this.col = col;
		}
		String getLowerBound() {
			return lowerBound;
		}
		void setLowerBound(String lowerBound) {
			this.lowerBound = lowerBound;
		}
		String getUpperBound() {
			return upperBound;
		}
		void setUpperBound(String upperBound) {
			this.upperBound = upperBound;
		}
	}
}