package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;

public class NotesFragment extends Fragment implements NoteAdapter.ItemActionListener {

	static final String NOTE_ID = "note id";
	private static final int REQUEST_CODE = 1;

	private NoteAdapter adapter;
	private List<Note> noteList;
	private List<DatabaseHelper.SortData> sortByList;
	private List<DatabaseHelper.FilterData> filterByList;

	private ImageButton expandFiltersButton;
	private boolean showingFilters;
	private FlexboxLayout filtersContainer;

	private Context context;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		context = v.getContext();

		RecyclerView recyclerView = v.findViewById(R.id.notes_recyclerView);
		FloatingActionButton addButton = v.findViewById(R.id.notes_btn_add);
		expandFiltersButton = v.findViewById(R.id.notes_btn_expand_filters);
		filtersContainer = v.findViewById(R.id.notes_filters_container);

		//set sort list
		sortByList = new ArrayList<>();
		SharedPreferences sortPref = context.getSharedPreferences(getString(R.string.sort_pref), Context.MODE_PRIVATE);
		try {
			JSONArray sortJSONArray = new JSONArray(sortPref.getString(getString(R.string.sort_key), ""));
			for (int i = 0; i < sortJSONArray.length(); i++) {
				JSONObject sd = sortJSONArray.getJSONObject(i);
				sortByList.add(new DatabaseHelper.SortData(sd.getString("col"), sd.getBoolean("ascending")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//set filter list
		filterByList = new ArrayList<>();
		SharedPreferences filterPref = context.getSharedPreferences(getString(R.string.filter_pref), Context.MODE_PRIVATE);
		try {
			JSONArray filterJSONArray = new JSONArray(filterPref.getString(getString(R.string.filter_key), ""));
			for (int i = 0; i < filterJSONArray.length(); i++) {
				JSONObject fd = filterJSONArray.getJSONObject(i);
				filterByList.add(new DatabaseHelper.FilterData(fd.getString("col"),
						fd.getString("upperBound"), fd.getString("lowerBound")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		reloadFilterChips();

		//set note list
		noteList = new DatabaseHelper(v.getContext()).getAll(sortByList, filterByList);

		//build RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		adapter = new NoteAdapter(noteList);
		adapter.setItemActionListener(this);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);

		//add button
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNote();
				openNote(noteList.size() - 1);
			}
		});

		//expand filters button
		showingFilters = false;
		expandFiltersButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expandOrCollapse();
			}
		});

		return v;
	}

	List<DatabaseHelper.SortData> getSortByList() {
		return sortByList;
	}

	private void expandOrCollapse() {
		if (showingFilters) {
			expandFiltersButton.setImageResource(R.drawable.ic_expand_more);
			hideFilters();
		} else {
			expandFiltersButton.setImageResource(R.drawable.ic_expand_less);
			showFilters();
		}
		showingFilters = !showingFilters;
	}

	private void showFilters() {
		filtersContainer.setVisibility(View.VISIBLE);
	}

	private void hideFilters() {
		filtersContainer.setVisibility(View.GONE);
	}

	private void addNote() {
		Note note = new Note(getContext());
		note.create();
		noteList.add(note);
		adapter.notifyItemInserted(noteList.size() - 1);
	}

	private void saveSortList() {
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sort_pref), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		JSONArray jsonArray = new JSONArray();
		for (DatabaseHelper.SortData sd : sortByList) {
			try {
				JSONObject sortJSON = new JSONObject();
				sortJSON.put("col", sd.getCol());
				sortJSON.put("ascending", sd.isAscending());
				jsonArray.put(sortJSON);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		editor.putString(getString(R.string.sort_key), jsonArray.toString());
		editor.apply();
	}

	private void saveFilterList() {
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.filter_pref), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		JSONArray jsonArray = new JSONArray();
		for (DatabaseHelper.FilterData fd : filterByList) {
			try {
				JSONObject filterJSON = new JSONObject();
				filterJSON.put("col", fd.getCol());
				filterJSON.put("upperBound", fd.getUpperBound());
				filterJSON.put("lowerBound", fd.getLowerBound());
				jsonArray.put(filterJSON);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		editor.putString(getString(R.string.filter_key), jsonArray.toString());
		editor.apply();
	}

	void sortNotes(List<DatabaseHelper.SortData> sortByList) {
		this.sortByList = sortByList;
		saveSortList();
		reloadNotes();
	}

	void addToFilters(DatabaseHelper.FilterData filterData) {
		filterByList.add(filterData);
		reloadNotes();
		reloadFilterChips();
		saveFilterList();
	}

	void removeFromFilters(DatabaseHelper.FilterData filterData) {
		filterByList.remove(filterData);
		reloadNotes();
		reloadFilterChips();
		saveFilterList();
	}

	private void reloadFilterChips() {
		filtersContainer.removeAllViews();
		for (DatabaseHelper.FilterData fd : filterByList) {
			filtersContainer.addView(getChip(fd));
		}
	}

	private Chip getChip(final DatabaseHelper.FilterData filterData) {
		final Chip chip = new Chip(context);
		chip.setText(filterData.getCol());
		chip.setChipBackgroundColorResource(R.color.colorForeground);
		chip.setTextColor(getResources().getColor(R.color.colorText));

		FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
				FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
		int marginStartEnd = (int)(2 * context.getResources().getDisplayMetrics().density); //convert 2dp to px
		int marginTopBot = -(int)(4 * context.getResources().getDisplayMetrics().density); //convert 4dp to px
		layoutParams.setMargins(marginStartEnd, marginTopBot, marginStartEnd, marginTopBot);
		chip.setLayoutParams(layoutParams);

		chip.setCloseIcon(getResources().getDrawable(R.drawable.ic_close));
		chip.setCloseIconTintResource(R.color.colorAccent);
		chip.setCloseIconVisible(true);
		chip.setOnCloseIconClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeFromFilters(filterData);
				filtersContainer.removeView(chip);
			}
		});

		chip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FilterBottomSheetDialog filterDialog = new FilterBottomSheetDialog(filterData);
				if (getFragmentManager() != null) {
					filterDialog.show(getFragmentManager(), "filterBottomSheet");
				}
			}
		});

		return chip;
	}

	private void reloadNotes() {
		noteList = new DatabaseHelper(getContext()).getAll(sortByList, filterByList);
		adapter.setList(noteList);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
			reloadNotes();
		}
	}

	//====================================================================================================
	//Note actions
	@Override
	public void openNote(int position) {
		Intent loadNoteEditor = new Intent(getActivity(), EditorActivity.class);
		loadNoteEditor.putExtra(NOTE_ID, noteList.get(position).getId());
		startActivityForResult(loadNoteEditor, REQUEST_CODE);
		Toast.makeText(context, "Opening " + noteList.get(position).getId(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
		holder.layout.setFocusable(false);
		holder.layout.setClickable(false);
		hideActionsOverlay(holder, context);

		final Note note = noteList.get(position);
		final String content = note.getContent();
		note.delete();
		noteList.remove(position);

		adapter.notifyItemRemoved(position);
		adapter.notifyItemRangeChanged(position, noteList.size());

		//Snackbar for undoing delete
		Snackbar.make(holder.deleteButton, "Undo deletion of \"" + note.getTitle() + "\"", Snackbar.LENGTH_LONG)
				.setAction("Undo", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						note.create();
						try {
							note.save(note.getTitle(), content, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
									Locale.getDefault()).parse(note.getDateModified()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						reloadNotes();
					}
				}).show();
	}

	//Actions overlay
	@Override
	public void showActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.VISIBLE);
		holder.cardView.setCardElevation(24 * context.getResources().getDisplayMetrics().density); //24dp to px
	}

	@Override
	public void hideActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.INVISIBLE);
		holder.cardView.setCardElevation(4 * context.getResources().getDisplayMetrics().density); //4dp to px
	}
}
