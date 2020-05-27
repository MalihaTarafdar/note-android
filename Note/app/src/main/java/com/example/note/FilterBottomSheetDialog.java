package com.example.note;

import android.content.Context;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {

	private LinearLayout dateCreatedLayout, dateModifiedLayout, characterCountLayout, wordCountLayout,
			paragraphCountLayout, readTimeLayout;

	private FilterListener listener;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

		final TextInputEditText startInput = v.findViewById(R.id.filter_input_start);
		final TextInputEditText endInput = v.findViewById(R.id.filter_input_end);
		Button applyButton = v.findViewById(R.id.filter_btn_apply);
		ImageButton closeButton = v.findViewById(R.id.filter_btn_close);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return v;
	}

	interface FilterListener {
		void onFilterApplied(List<DatabaseHelper.FilterData> filterByList);
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
