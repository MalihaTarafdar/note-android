package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		drawer = findViewById(R.id.drawer_layout);

		//set toolbar
		setSupportActionBar(toolbar);
		//set toolbar background
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.appbar_bg));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Navigation drawer opened", Toast.LENGTH_SHORT).show();
			}
		});

		//action bar navigation drawer
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
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
