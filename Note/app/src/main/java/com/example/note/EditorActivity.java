package com.example.note;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

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
}
