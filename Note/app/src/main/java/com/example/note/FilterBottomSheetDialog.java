package com.example.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {

	private LinearLayout dateCreatedLayout, dateModifiedLayout, characterCountLayout, wordCountLayout,
			paragraphCountLayout, readTimeLayout;
	private Button applyButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

		dateCreatedLayout = v.findViewById(R.id.filter_date_created);
		dateModifiedLayout = v.findViewById(R.id.filter_date_modified);
		characterCountLayout = v.findViewById(R.id.filter_character_count);
		wordCountLayout = v.findViewById(R.id.filter_word_count);
		paragraphCountLayout = v.findViewById(R.id.filter_paragraph_count);
		readTimeLayout = v.findViewById(R.id.filter_read_time);
		applyButton = v.findViewById(R.id.filter_btn_apply);
		ImageButton closeButton = v.findViewById(R.id.filter_btn_close);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return v;
	}
}
