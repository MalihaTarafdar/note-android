package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class NotesFragment extends Fragment implements NoteAdapter.ItemActionListener {

	static final String NOTE_ID = "note id";
	private static final int REQUEST_CODE = 1;

	private NoteAdapter adapter;
	private List<Note> noteList;
	private List<DatabaseHelper.SortData> sortByList;
	private List<DatabaseHelper.FilterData> filterByList;
	private Note noteToExport;
	private String searchQuery;

	private ImageButton expandFiltersButton;
	private FlexboxLayout filtersContainer;
	private TextView noNotesView;

	private Context context;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		context = v.getContext();
		searchQuery = "";

		RecyclerView recyclerView = v.findViewById(R.id.notes_recyclerView);
		FloatingActionButton addButton = v.findViewById(R.id.notes_btn_add);
		expandFiltersButton = v.findViewById(R.id.notes_btn_expand_filters);
		filtersContainer = v.findViewById(R.id.notes_filters_container);

		//set sort list from saved preferences
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

		//set filter list from saved preferences
		filterByList = new ArrayList<>();
		SharedPreferences filterPref = context.getSharedPreferences(getString(R.string.filter_pref), Context.MODE_PRIVATE);
		try {
			JSONArray filterJSONArray = new JSONArray(filterPref.getString(getString(R.string.filter_key), ""));
			for (int i = 0; i < filterJSONArray.length(); i++) {
				JSONObject fd = filterJSONArray.getJSONObject(i);
				filterByList.add(new DatabaseHelper.FilterData(fd.getString("col"),
						fd.getString("lowerBound"), fd.getString("upperBound")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		reloadFilterChips();

		//set note list
		noteList = new DatabaseHelper(v.getContext()).getAll(sortByList, filterByList);
		noNotesView = v.findViewById(R.id.notes_none);
		showOrHideNoNotesView();

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
		expandFiltersButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expandOrCollapseFilters();
			}
		});

		return v;
	}

	List<DatabaseHelper.SortData> getSortByList() {
		return sortByList;
	}

	void filterBySearch(String searchQuery) {
		this.searchQuery = searchQuery;
		reloadNotes();
	}

	private void reloadNotes() {
		//get notes from database
		noteList = new DatabaseHelper(getContext()).getAll(sortByList, filterByList);
		//filter notes by search query in content and title
		for (int i = 0; i < noteList.size(); i++) {
			if (!noteList.get(i).getContent().contains(searchQuery) &&
					!noteList.get(i).getTitle().contains(searchQuery)) {
				noteList.remove(i);
				i--;
			}
		}

		adapter.setList(noteList);
		adapter.notifyDataSetChanged();
		showOrHideNoNotesView();
	}

	private void expandOrCollapseFilters() {
		if (filtersContainer.getVisibility() == View.VISIBLE) {
			expandFiltersButton.setImageResource(R.drawable.ic_expand_more);
			hideFilters();
		} else {
			expandFiltersButton.setImageResource(R.drawable.ic_expand_less);
			showFilters();
		}
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
			filtersContainer.addView(getFilterChip(fd));
		}
	}

	private Chip getFilterChip(final DatabaseHelper.FilterData filterData) {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
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

		showOrHideNoNotesView();

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

	@Override
	public void exportNote(int position) {
		noteToExport = noteList.get(position);
		ExportBottomSheetDialog exportDialog = new ExportBottomSheetDialog(noteList.get(position));
		exportDialog.show(getFragmentManager(), "exportBottomSheet");
	}

	Note getNoteToExport() {
		return noteToExport;
	}

	private void showOrHideNoNotesView() {
		if (noteList.size() == 0) noNotesView.setVisibility(View.VISIBLE);
		else noNotesView.setVisibility(View.INVISIBLE);
	}

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
