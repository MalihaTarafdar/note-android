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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		drawer = findViewById(R.id.drawer_layout);
		NavigationView navView = findViewById(R.id.nav_view);

		//set toolbar
		setSupportActionBar(toolbar);
		//set toolbar background
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.appbar_bg));

		//action bar navigation drawer
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		//navigation view
		//replace fragment on item select
		navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.nav_notes:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
								new NotesFragment()).commit();
						break;
					case R.id.nav_settings:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
								new SettingsFragment()).commit();
						break;
				}
				drawer.closeDrawer(GravityCompat.START);
				return true;
			}
		});

		//open notes fragment when app starts
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					new NotesFragment()).commit();
			navView.setCheckedItem(R.id.nav_notes);
		}
	}

	@Override
	public void onBackPressed() {
		//close navigation drawer on back press
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
		//toolbar option items
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
