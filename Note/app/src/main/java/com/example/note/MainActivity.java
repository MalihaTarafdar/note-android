package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SortBottomSheetDialog.SortListener, FilterBottomSheetDialog.FilterListener {

	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Toolbar toolbar = findViewById(R.id.toolbar);
		drawer = findViewById(R.id.drawer_layout);
		NavigationView navView = findViewById(R.id.nav_view);

		//set toolbar
		setSupportActionBar(toolbar);

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
						invalidateOptionsMenu();
						toolbar.setTitle(getString(R.string.notes));
						break;
					case R.id.nav_settings:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
								new SettingsFragment()).commit();
						invalidateOptionsMenu();
						toolbar.setTitle(getString(R.string.settings));
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
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
				instanceof NotesFragment) {
			getMenuInflater().inflate(R.menu.menu_main, menu);

			MenuItem searchItem = menu.findItem(R.id.menu_main_search);
			SearchView searchView = (SearchView) searchItem.getActionView();
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		//toolbar option items
		switch (item.getItemId()) {
			case R.id.menu_main_search:
				return true;
			case R.id.menu_main_sort:
				SortBottomSheetDialog sortDialog = new SortBottomSheetDialog();
				sortDialog.show(getSupportFragmentManager(), "sortBottomSheet");
				return true;
			case R.id.menu_main_filter:
				FilterBottomSheetDialog filterDialog = new FilterBottomSheetDialog();
				filterDialog.show(getSupportFragmentManager(), "filterBottomSheet");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSortApplied(List<DatabaseHelper.SortData> sortByList) {
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
				instanceof NotesFragment) {
			NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
			if (notesFragment != null) notesFragment.sortNotes(sortByList);
		}
	}

	@Override
	public void onFilterApplied(List<DatabaseHelper.FilterData> filterByList) {
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
				instanceof NotesFragment) {
			NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
			if (notesFragment != null) notesFragment.filterNotes(filterByList);
		}
	}
}
