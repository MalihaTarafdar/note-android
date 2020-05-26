package com.example.note;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

	private LinearLayout titleLayout, dateCreatedLayout, dateModifiedLayout, characterCountLayout,
			wordCountLayout, paragraphCountLayout, readTimeLayout;

	private SortListener listener;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_sort, container, false);

		titleLayout = v.findViewById(R.id.sort_title);
		dateCreatedLayout = v.findViewById(R.id.sort_date_created);
		dateModifiedLayout = v.findViewById(R.id.sort_date_modified);
		characterCountLayout = v.findViewById(R.id.sort_character_count);
		wordCountLayout = v.findViewById(R.id.sort_word_count);
		paragraphCountLayout = v.findViewById(R.id.sort_paragraph_count);
		readTimeLayout = v.findViewById(R.id.sort_read_time);
		ImageButton closeButton = v.findViewById(R.id.sort_btn_close);

		titleLayout.setOnClickListener(this);
		dateCreatedLayout.setOnClickListener(this);
		dateModifiedLayout.setOnClickListener(this);
		characterCountLayout.setOnClickListener(this);
		wordCountLayout.setOnClickListener(this);
		paragraphCountLayout.setOnClickListener(this);
		readTimeLayout.setOnClickListener(this);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return v;
	}

	@Override
	public void onClick(View v) {
		if (v == titleLayout) {
			listener.onClick(NoteDAO.SortOption.TITLE);
		} else if (v == dateCreatedLayout) {
			listener.onClick(NoteDAO.SortOption.DATE_CREATED);
		} else if (v == dateModifiedLayout) {
			listener.onClick(NoteDAO.SortOption.DATE_MODIFIED);
		} else if (v == characterCountLayout) {
			listener.onClick(NoteDAO.SortOption.CHARACTER_COUNT);
		} else if (v == wordCountLayout) {
			listener.onClick(NoteDAO.SortOption.WORD_COUNT);
		} else if (v == paragraphCountLayout) {
			listener.onClick(NoteDAO.SortOption.PARAGRAPH_COUNT);
		} else if (v == readTimeLayout) {
			listener.onClick(DatabaseHelper.SortOption.READ_TIME);
		}
		dismiss();
	}

	interface SortListener {
		void onClick(NoteDAO.SortOption sortOption);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (SortListener)context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement ItemClickListener");
		}
	}
}
