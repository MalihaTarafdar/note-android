package com.example.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class ExportBottomSheetDialog extends BottomSheetDialogFragment {

	private ExportSelectedListener listener;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_export, container, false);

		ImageButton closeButton = v.findViewById(R.id.export_btn_close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		HashMap<Integer, String> exportOptions = new HashMap<Integer, String>() {{
			put(R.id.export_txt, "txt");
			put(R.id.export_md, "md");
		}};

		for (final Map.Entry<Integer, String> entry : exportOptions.entrySet()) {
			v.findViewById(entry.getKey()).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onExportItemSelected(entry.getValue());
				}
			});
		}

		return v;
	}

	interface ExportSelectedListener {
		void onExportItemSelected(String ext);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (ExportSelectedListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement SortListener");
		}
	}
}
