package com.example.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortBottomSheetDialog extends BottomSheetDialogFragment {

	LinearLayout titleLayout, dateCreatedLayout, dateModifiedLayout, characterCountLayout,
			wordCountLayout, paragraphCountLayout, readTimeLayout;

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

		return v;
	}
}
