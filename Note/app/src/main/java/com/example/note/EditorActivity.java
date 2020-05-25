package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

	Note note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
		note = databaseHelper.getNoteById(getIntent().getIntExtra(NotesFragment.NOTE_ID, -1));

		//set title
		setTitle(note.getTitle());

		//set toolbar
		Toolbar toolbar = findViewById(R.id.editor_toolbar);
		setSupportActionBar(toolbar);
		//set toolbar up action
		ActionBar appBar = getSupportActionBar();
		if (appBar != null) appBar.setDisplayHomeAsUpEnabled(true);

		//show note
		EditText titleEditText = findViewById(R.id.editor_note_title);
		EditText contentEditText = findViewById(R.id.editor_note_content);

		titleEditText.setText(note.getTitle());
		contentEditText.setText(note.getContent());
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
