package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static com.example.note.StorageHelper.CREATE_FILE;

public class MainActivity extends AppCompatActivity implements SortBottomSheetDialog.SortListener,
		FilterBottomSheetDialog.FilterListener {

	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//set saved note id count
		SharedPreferences pref = getSharedPreferences(getString(R.string.id_pref), Context.MODE_PRIVATE);
		Note.curId = pref.getInt(getString(R.string.id_key), 0);

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

			final NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);

			searchView.setQueryHint("Search");
			searchView.setOnCloseListener(new SearchView.OnCloseListener() {
				@Override
				public boolean onClose() {
					if (notesFragment != null) notesFragment.filterBySearch("");
					return false;
				}
			});
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					if (notesFragment != null) notesFragment.filterBySearch(query);
					return false;
				}
				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}
			});
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		//toolbar option items
		switch (item.getItemId()) {
			case R.id.menu_main_search:
				return true;
			case R.id.menu_main_sort: //sort
				NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container);
				if (notesFragment != null) {
					//pass sort list from notes fragment
					SortBottomSheetDialog sortDialog = new SortBottomSheetDialog(notesFragment.getSortByList());
					sortDialog.show(getSupportFragmentManager(), "sortBottomSheet");
				}
				return true;
			case R.id.menu_main_filter: //filter
				FilterBottomSheetDialog filterDialog = new FilterBottomSheetDialog();
				filterDialog.show(getSupportFragmentManager(), "filterBottomSheet");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CREATE_FILE && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			if (uri == null || uri.getPath() == null) return;
			NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
			if (notesFragment != null) notesFragment.getNoteToExport().export(uri, ExportBottomSheetDialog.ext);
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
	public void onFilterAdded(DatabaseHelper.FilterData filterData) {
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
				instanceof NotesFragment) {
			NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
			if (notesFragment != null) notesFragment.addToFilters(filterData);
		}
	}

	@Override
	public void onFilterSaved(DatabaseHelper.FilterData oldFilterData, DatabaseHelper.FilterData filterData) {
		if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
				instanceof NotesFragment) {
			NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
			if (notesFragment != null) {
				notesFragment.removeFromFilters(oldFilterData);
				notesFragment.addToFilters(filterData);
			}
		}
	}
}
