package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {

	Note note;

	EditText etTitle, etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
		note = dbHelper.getNoteById(getIntent().getIntExtra(NotesFragment.NOTE_ID, -1));

		//set title
		setTitle(note.getTitle());

		//set toolbar
		Toolbar toolbar = findViewById(R.id.editor_toolbar);
		setSupportActionBar(toolbar);
		//set toolbar up action
		ActionBar appBar = getSupportActionBar();
		if (appBar != null) appBar.setDisplayHomeAsUpEnabled(true);

		//show note
		etTitle = findViewById(R.id.editor_note_title);
		etContent = findViewById(R.id.editor_note_content);

		//set title and content in editor
		etTitle.setText(note.getTitle());
		Log.d("TAG", "Title: " + note.getTitle());
		Log.d("TAG", "Content: " + note.getContent());
		etContent.setText(note.getContent());

		//auto-save every 30 seconds
		final long AUTO_SAVE_INTERVAL = 30000;
		final Handler autoSaveHandler = new Handler();
		Runnable autoSaveRunnable = new Runnable() {
			@Override
			public void run() {
				Log.d("TAG", "auto-save");
				note.save(etTitle.getText().toString(), etContent.getText().toString(),
						Calendar.getInstance(Locale.getDefault()).getTime());
				autoSaveHandler.postDelayed(this, AUTO_SAVE_INTERVAL);
			}
		};
		autoSaveHandler.postDelayed(autoSaveRunnable, 0);
	}

	@Override
	public void onBackPressed() {
		//save when leaving editor
		Log.d("TAG", "save on back press");
		note.save(etTitle.getText().toString(), etContent.getText().toString(),
				Calendar.getInstance(Locale.getDefault()).getTime());
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_editor_export:
				Toast.makeText(this, "Export item selected", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_editor_delete:
				Toast.makeText(this, "Delete item selected", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_editor_info:
				Toast.makeText(this, "Info item selected", Toast.LENGTH_SHORT).show();
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}
}
