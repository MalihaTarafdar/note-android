package com.example.note;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

	private RecyclerView recyclerView;
	private FloatingActionButton addButton;

	private static NoteAdapter adapter;
	private static ArrayList<NoteItem> noteList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		recyclerView = v.findViewById(R.id.notes_recyclerView);
		addButton = v.findViewById(R.id.notes_btn_add);

		//create note list
		noteList = new ArrayList<>();
		noteList.add(new NoteItem("Note Title 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));

		//build RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		adapter = new NoteAdapter(noteList);
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

	//=====================================================================================
	//Note actions
	private static void addNote() {
		//TEMP NOTE DATA FOR TESTING ADD
		int ind = 1;
		if (noteList.size() > 0) {
			String lastTitle = noteList.get(noteList.size() - 1).getTitle();
			ind = Integer.parseInt(lastTitle.substring(lastTitle.length() - 1)) + 1;
		}
		noteList.add(new NoteItem("Note Title " + ind, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
		adapter.notifyItemInserted(noteList.size() - 1);
	}

	private static void deleteNote(NoteAdapter.NoteViewHolder holder, int position, Context context) {
		holder.layout.setFocusable(false);
		holder.layout.setClickable(false);
		hideActionsOverlay(holder, context);
		noteList.remove(position);
		adapter.notifyItemRemoved(position);
		adapter.notifyItemRangeChanged(position, noteList.size());
	}

	//Actions overlay
	private static void showActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.VISIBLE);
		holder.cardView.setCardElevation(24 * context.getResources().getDisplayMetrics().density); //24dp to px
	}

	private static void hideActionsOverlay(NoteAdapter.NoteViewHolder holder, Context context) {
		holder.overlay.setVisibility(View.INVISIBLE);
		holder.cardView.setCardElevation(4 * context.getResources().getDisplayMetrics().density); //4dp to px
	}
	//=====================================================================================
	//RecyclerView Custom Adapter
	public static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

		private ArrayList<NoteItem> list;
		private Context context;

		NoteAdapter(ArrayList<NoteItem> noteList) {
			this.list = noteList;
		}

		@NonNull
		@Override
		public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			context = parent.getContext();
			View v = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
			return new NoteViewHolder(v);
		}

		@Override
		public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
			NoteItem curItem = list.get(position);

			holder.titleView.setText(curItem.getTitle());
			holder.previewView.setText(curItem.getPreview());

			//note click listener
			holder.layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, holder.titleView.getText() + " clicked", Toast.LENGTH_SHORT).show();
				}
			});

			//show overlay
			holder.overflowButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showActionsOverlay(holder, context);
				}
			});

			//overlay actions
			//delete note
			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteNote(holder, position, context);
				}
			});
			//export note
			holder.exportButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Export button pressed", Toast.LENGTH_SHORT).show();
				}
			});
			//hide overlay
			holder.cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hideActionsOverlay(holder, context);
				}
			});
		}

		@Override
		public int getItemCount() {
			return list.size();
		}

		//=====================================================================================
		//RecyclerView custom ViewHolder
		static class NoteViewHolder extends RecyclerView.ViewHolder {
			CardView cardView;
			TextView titleView, previewView;
			ImageButton overflowButton, deleteButton, exportButton, cancelButton;
			ConstraintLayout layout;
			LinearLayout overlay;

			NoteViewHolder(@NonNull View itemView) {
				super(itemView);

				cardView = itemView.findViewById(R.id.item_card);
				titleView = itemView.findViewById(R.id.item_title);
				previewView = itemView.findViewById(R.id.item_preview);
				overflowButton = itemView.findViewById(R.id.item_btn_overflow);
				deleteButton = itemView.findViewById(R.id.item_btn_delete);
				exportButton = itemView.findViewById(R.id.item_btn_export);
				cancelButton = itemView.findViewById(R.id.item_btn_cancel);
				layout = itemView.findViewById(R.id.item_layout);
				overlay = itemView.findViewById(R.id.item_overlay);
			}
		}
	}

	//=====================================================================================
	//RecyclerView item
	public static class NoteItem {
		private String title;
		private String preview;

		NoteItem(String title, String preview) {
			this.title = title;
			this.preview = preview;
		}

		String getTitle() {
			return title;
		}

		String getPreview() {
			return preview;
		}
	}

}
