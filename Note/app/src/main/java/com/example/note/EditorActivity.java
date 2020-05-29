package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity implements ExportBottomSheetDialog.ExportSelectedListener {

	private Note note;
	private EditText etTitle, etContent;

	private Handler autoSaveHandler;
	private long AUTO_SAVE_INTERVAL;

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
		etContent.setText(note.getContent());

		//auto-save every 60 seconds
		AUTO_SAVE_INTERVAL = 60000;
		autoSaveHandler = new Handler();
		autoSaveHandler.postDelayed(autoSaveRunnable, 0);
	}

	private Runnable autoSaveRunnable = new Runnable() {
		@Override
		public void run() {
			saveNote();
			autoSaveHandler.postDelayed(this, AUTO_SAVE_INTERVAL);
		}
	};

	public Note getNote() {
		return note;
	}

	@Override
	protected void onPause() {
		super.onPause();
		//stop auto save when leaving editor
		autoSaveHandler.removeCallbacks(autoSaveRunnable);
		//save when leaving editor
		saveNote();
		setResult(RESULT_OK);
		finish();
	}

	private void saveNote() {
		//only save note when there is new content to save
		if (!note.getTitle().equals(etTitle.getText().toString()) ||
				!note.getContent().equals(etContent.getText().toString())) {
			note.save(etTitle.getText().toString(), etContent.getText().toString(),
					Calendar.getInstance(Locale.getDefault()).getTime());
		}
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		//save note before performing actions on it
		saveNote();

		switch (item.getItemId()) {
			case R.id.menu_editor_export: //export
				ExportBottomSheetDialog exportDialog = new ExportBottomSheetDialog();
				exportDialog.show(getSupportFragmentManager(), "exportBottomSheet");
				return true;
			case R.id.menu_editor_delete: //delete
				new MaterialAlertDialogBuilder(EditorActivity.this)
						.setTitle("Delete Note")
						.setMessage("Would you like to delete this note?")
						.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}})
						.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								note.delete();
								setResult(RESULT_OK);
								finish();
							}
						}).show();
				return true;
			case R.id.menu_editor_info: //info
				InfoBottomSheetDialog infoDialog = new InfoBottomSheetDialog();
				infoDialog.show(getSupportFragmentManager(), "infoBottomSheet");
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}

	@Override
	public void onExportItemSelected(String ext) {
		saveNote();
		//export note
		//FIXME
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
		File dst = new File(dir, note.getTitle().replaceAll(" ", "_") + "." + ext);
		try (FileOutputStream fos = this.openFileOutput(dst.getName(), Context.MODE_PRIVATE)) {
			fos.write(etContent.getText().toString().getBytes());
			Toast.makeText(this, dst.getAbsolutePath(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
