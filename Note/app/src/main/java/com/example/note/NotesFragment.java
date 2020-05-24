package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotesFragment extends Fragment {

	private NoteAdapter adapter;
	private List<Note> noteList;
	static final String NOTE_ID = "note id";

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		RecyclerView recyclerView = v.findViewById(R.id.notes_recyclerView);
		FloatingActionButton addButton = v.findViewById(R.id.notes_btn_add);

		//list
		noteList = new DatabaseHelper(v.getContext()).getAll();

		//build RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		adapter = new NoteAdapter(noteList);
		adapter.setItemActionListener(new NoteAdapter.ItemActionListener() {
			@Override
			public void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
				NotesFragment.this.deleteNote(holder, position, context);
			}
			@Override
			public void openNote(int position) {
				NotesFragment.this.openNote(position);
			}
			@Override
			public void showActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
				NotesFragment.this.showActionsOverlay(holder, context);
			}
			@Override
			public void hideActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
				NotesFragment.this.hideActionsOverlay(holder, context);
			}
		});
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);

		//add button
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNote();
			}
		});

		return v;
	}

	//====================================================================================================
	//Note actions
	private void addNote() {
		Note note = new Note(getContext());
		noteList.add(note);
		adapter.notifyItemInserted(noteList.size() - 1);
	}

	private void openNote(int position) {
		Intent loadNoteEditor = new Intent(getActivity(), EditorActivity.class);
		loadNoteEditor.putExtra(NOTE_ID, noteList.get(position).getId());
		startActivity(loadNoteEditor);
	}

	private void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
		holder.layout.setFocusable(false);
		holder.layout.setClickable(false);
		hideActionsOverlay(holder, context);

		noteList.get(position).delete();
		noteList.remove(position);

		adapter.notifyItemRemoved(position);
		adapter.notifyItemRangeChanged(position, noteList.size());
	}

	//Actions overlay
	private void showActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.VISIBLE);
		holder.cardView.setCardElevation(24 * context.getResources().getDisplayMetrics().density); //24dp to px
	}

	private void hideActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.INVISIBLE);
		holder.cardView.setCardElevation(4 * context.getResources().getDisplayMetrics().density); //4dp to px
	}
}
