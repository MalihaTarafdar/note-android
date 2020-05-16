package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//set toolbar
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		//set toolbar background
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.appbar_bg));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_search:
				Toast.makeText(this, "Search item selected", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_sort:
				Toast.makeText(this, "Sort item selected", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_filter:
				Toast.makeText(this, "Filter item selected", Toast.LENGTH_SHORT).show();
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
}
