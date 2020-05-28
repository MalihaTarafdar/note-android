package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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

		//list
		sortByList = new ArrayList<>();
		filterByList = new ArrayList<>();
		noteList = new DatabaseHelper(v.getContext()).getAll(sortByList, filterByList);

		//build RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		adapter = new NoteAdapter(noteList);
		adapter.setItemActionListener(this);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);

		//add a note if there are none
		if (noteList.size() == 0) addNote();

		//add button
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNote();
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
			expandFiltersButton.setImageResource(R.drawable.ic_expand_less);
			hideFilters();
		} else {
			expandFiltersButton.setImageResource(R.drawable.ic_expand_more);
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

	void sortNotes(List<DatabaseHelper.SortData> sortByList) {
		this.sortByList = sortByList;
		reloadNotes();
	}

	void addToFilters(DatabaseHelper.FilterData filterData) {
		filterByList.add(filterData);
		reloadNotes();
		reloadFilterChips();
	}

	void removeFromFilters(DatabaseHelper.FilterData filterData) {
		filterByList.remove(filterData);
		reloadNotes();
		reloadFilterChips();
	}

	private void reloadFilterChips() {
		filtersContainer.removeAllViews();
		for (DatabaseHelper.FilterData fd : filterByList) {
			filtersContainer.addView(getChip(fd));
		}
	}

	Chip getChip(final DatabaseHelper.FilterData filterData) {
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
			adapter.notifyDataSetChanged();
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

		noteList.get(position).delete();
		noteList.remove(position);

		adapter.notifyItemRemoved(position);
		adapter.notifyItemRangeChanged(position, noteList.size());
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
