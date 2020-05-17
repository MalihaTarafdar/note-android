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

import java.util.ArrayList;

public class NotesFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_notes, container, false);

		RecyclerView recyclerView = v.findViewById(R.id.rv_notes);

		//note list
		ArrayList<NoteItem> noteList = new ArrayList<>();
		noteList.add(new NoteItem("Note Title 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
		noteList.add(new NoteItem("Note Title 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
		noteList.add(new NoteItem("Note Title 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
		noteList.add(new NoteItem("Note Title 4", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));

		//RecyclerView
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
		RecyclerView.Adapter adapter = new NoteAdapter(noteList);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);

		return v;
	}

	//RecyclerView Custom Adapter
	public static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

		private ArrayList<NoteItem> noteList;
		private Context context;

		NoteAdapter(ArrayList<NoteItem> noteList) {
			this.noteList = noteList;
		}

		@NonNull
		@Override
		public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			context = parent.getContext();
			View v = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
			return new NoteViewHolder(v);
		}

		@Override
		public void onBindViewHolder(@NonNull final NoteViewHolder holder, int position) {
			NoteItem curItem = noteList.get(position);

			holder.titleView.setText(curItem.getTitle());
			holder.previewView.setText(curItem.getPreview());

			holder.overflowButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					holder.itemOverlay.setVisibility(View.VISIBLE);
				}
			});
			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Delete button pressed", Toast.LENGTH_SHORT).show();
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
			return noteList.size();
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
