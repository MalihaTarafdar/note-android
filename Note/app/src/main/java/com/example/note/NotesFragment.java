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

import java.util.ArrayList;

public class NotesFragment extends Fragment {

	private NoteAdapter adapter;
	private ArrayList<NoteItem> noteList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		RecyclerView recyclerView = v.findViewById(R.id.notes_recyclerView);
		FloatingActionButton addButton = v.findViewById(R.id.notes_btn_add);

		//create note list
		noteList = new ArrayList<>();
		//testing item
		noteList.add(new NoteItem("Note Title 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));

		//build RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		adapter = new NoteAdapter(noteList);
		adapter.setItemActionListener(new NoteAdapter.ItemActionListener() {
			@Override
			public void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
				NotesFragment.this.deleteNote(holder, position, context);
			}
			@Override
			public void openNote() {
				NotesFragment.this.openNote();
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
		//TEMP NOTE DATA FOR TESTING ADD
		int ind = 1;
		if (noteList.size() > 0) {
			String lastTitle = noteList.get(noteList.size() - 1).getTitle();
			ind = Integer.parseInt(lastTitle.substring(lastTitle.length() - 1)) + 1;
		}
		noteList.add(new NoteItem("Note Title " + ind, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
		adapter.notifyItemInserted(noteList.size() - 1);
	}

	private void openNote() {
		Intent loadNoteEditor = new Intent(getActivity(), EditorActivity.class);
		startActivity(loadNoteEditor);
	}

	private void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
		holder.layout.setFocusable(false);
		holder.layout.setClickable(false);
		hideActionsOverlay(holder, context);
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
