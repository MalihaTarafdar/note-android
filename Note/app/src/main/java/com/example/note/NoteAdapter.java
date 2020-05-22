package com.example.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//RecyclerView Custom Adapter
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

	private ArrayList<NoteItem> list;
	private Context context;
	private ItemActionListener listener;

	NoteAdapter(ArrayList<NoteItem> noteList) {
		this.list = noteList;
	}

	void setItemActionListener(ItemActionListener listener) {
		this.listener = listener;
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

		//open note on item click
		holder.layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.openNote(position);
				Toast.makeText(context, holder.titleView.getText() + " clicked", Toast.LENGTH_SHORT).show();
			}
		});

		//show overlay on overflow button click
		holder.overflowButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.showActionsOverlay(holder, context);
			}
		});

		//overlay actions
		//delete note
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.deleteNote(holder, position, context);
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
				listener.hideActionsOverlay(holder, context);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	//====================================================================================================
	//listener for note item actions
	public interface ItemActionListener {
		void deleteNote(NoteViewHolder holder, int position, Context context);
		void openNote(int position);
		void showActionsOverlay(NoteViewHolder holder, Context context);
		void hideActionsOverlay(NoteViewHolder holder, Context context);
	}

	//====================================================================================================
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