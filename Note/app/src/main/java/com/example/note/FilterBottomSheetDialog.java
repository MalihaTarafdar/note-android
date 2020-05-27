package com.example.note;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

	private LinearLayout dateCreatedLayout, dateModifiedLayout, characterCountLayout, wordCountLayout,
			paragraphCountLayout, readTimeLayout;

	private FilterListener listener;
	private NoteDAO.FilterOption filterOption;

	private final HashMap<Integer, NoteDAO.FilterOption> OPTIONS = new HashMap<Integer, NoteDAO.FilterOption>() {{
		put(R.id.filter_title, NoteDAO.FilterOption.TITLE);
		put(R.id.filter_date_created, NoteDAO.FilterOption.DATE_CREATED);
		put(R.id.filter_date_modified, NoteDAO.FilterOption.DATE_MODIFIED);
		put(R.id.filter_character_count, NoteDAO.FilterOption.CHARACTER_COUNT);
		put(R.id.filter_word_count, NoteDAO.FilterOption.WORD_COUNT);
		put(R.id.filter_paragraph_count, NoteDAO.FilterOption.PARAGRAPH_COUNT);
		put(R.id.filter_read_time, NoteDAO.FilterOption.READ_TIME);
	}};

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

		final TextInputEditText startInput = v.findViewById(R.id.filter_input_start);
		final TextInputEditText endInput = v.findViewById(R.id.filter_input_end);
		Button applyButton = v.findViewById(R.id.filter_btn_apply);
		ImageButton closeButton = v.findViewById(R.id.filter_btn_close);

		for (Map.Entry<Integer, NoteDAO.FilterOption> entry : OPTIONS.entrySet()) {
			v.findViewById(entry.getKey()).setOnClickListener(this);
		}

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(filterOption, startInput.getText().toString(), endInput.getText().toString());
				dismiss();
			}
		});

		return v;
	}

	@Override
	public void onClick(View v) {
		filterOption = OPTIONS.get(v.getId());
		for (Map.Entry<Integer, NoteDAO.FilterOption> entry : OPTIONS.entrySet()) {
			TypedValue outValue = new TypedValue();
			getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
			v.getRootView().findViewById(entry.getKey()).setBackgroundResource(outValue.resourceId);
		}
		v.setBackgroundColor(getResources().getColor(R.color.colorBackground));
	}

	interface FilterListener {
		void onClick(NoteDAO.FilterOption filterOption, String start, String end);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (FilterListener)context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement FilterListener");
		}
	}
}
