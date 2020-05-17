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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

	RecyclerView recyclerView;
	FloatingActionButton addButton;

	static RecyclerView.Adapter adapter;
	static ArrayList<NoteItem> noteList;

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
				//TEMP NOTE DATA FOR TESTING ADD
				int ind = 0;
				if (noteList.size() > 0) {
					String lastTitle = noteList.get(noteList.size() - 1).getTitle();
					ind = Integer.parseInt(lastTitle.substring(lastTitle.length() - 1)) + 1;
				}
				noteList.add(new NoteItem("Note Title " + ind, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
				adapter.notifyItemInserted(noteList.size() - 1);
			}
		});

		return v;
	}

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
			holder.overflowButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.itemOverlay.setVisibility(View.VISIBLE);
				}
			});

			//overlay actions
			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.itemOverlay.setVisibility(View.INVISIBLE);
					noteList.remove(position);
					adapter.notifyItemRemoved(position);
					adapter.notifyItemRangeChanged(position, noteList.size());
				}
			});
			holder.exportButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Export button pressed", Toast.LENGTH_SHORT).show();
				}
			});
			holder.cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.itemOverlay.setVisibility(View.INVISIBLE);
				}
			});
		}

		@Override
		public int getItemCount() {
			return list.size();
		}

		//RecyclerView custom ViewHolder
		static class NoteViewHolder extends RecyclerView.ViewHolder {
			TextView titleView, previewView;
			ImageButton overflowButton, deleteButton, exportButton, cancelButton;
			LinearLayout itemOverlay;

			NoteViewHolder(@NonNull View itemView) {
				super(itemView);

				titleView = itemView.findViewById(R.id.item_title);
				previewView = itemView.findViewById(R.id.item_preview);
				overflowButton = itemView.findViewById(R.id.item_btn_overflow);
				deleteButton = itemView.findViewById(R.id.item_btn_delete);
				exportButton = itemView.findViewById(R.id.item_btn_export);
				cancelButton = itemView.findViewById(R.id.item_btn_cancel);
				itemOverlay = itemView.findViewById(R.id.item_overlay);
			}
		}
	}

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
