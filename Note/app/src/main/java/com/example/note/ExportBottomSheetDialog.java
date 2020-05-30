package com.example.note;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class ExportBottomSheetDialog extends BottomSheetDialogFragment {

	private static final int STORAGE_PERMISSION_REQUEST = 1, CREATE_FILE = 1;
	private Note note;
	private ExportSelectedListener listener;

	ExportBottomSheetDialog(Note note) {
		this.note = note;
	}

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

		//read and write to storage permission
		if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
					STORAGE_PERMISSION_REQUEST);
		}

		for (final Map.Entry<Integer, String> entry : exportOptions.entrySet()) {
			v.findViewById(entry.getKey()).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onExportSelected(entry.getValue());

					Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("*/*");
					intent.putExtra(Intent.EXTRA_TITLE, note.getTitle().replaceAll(" ", "_") + "." + entry.getValue());

					getActivity().startActivityForResult(intent, CREATE_FILE);
					dismiss();
				}
			});
		}

		return v;
	}

	interface ExportSelectedListener {
		void onExportSelected(String ext);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (ExportSelectedListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement ExportSelectedListener");
		}
	}
}
