package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		Toolbar toolbar = findViewById(R.id.editor_toolbar);

		//set toolbar
		setSupportActionBar(toolbar);
		//set toolbar up action
		ActionBar appBar = getSupportActionBar();
		appBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_editor_find:
				Toast.makeText(this, "Find item selected", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_editor_export:
				Toast.makeText(this, "Export item selected", Toast.LENGTH_SHORT).show();
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
