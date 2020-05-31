package com.example.note;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoBottomSheetDialog extends BottomSheetDialogFragment {

	private Note note;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_info, container, false);

		TextView dateCreatedView = v.findViewById(R.id.info_date_created_view);
		TextView dateModifiedView = v.findViewById(R.id.info_date_modified_view);
		TextView characterCountView = v.findViewById(R.id.info_character_count_view);
		TextView wordCountView = v.findViewById(R.id.info_word_count_view);
		TextView paragraphCountView = v.findViewById(R.id.info_paragraph_count_view);
		TextView readTimeView = v.findViewById(R.id.info_read_time_view);
		ImageButton closeButton = v.findViewById(R.id.info_btn_close);

		dateCreatedView.setText(formatDate(note.getDateCreated()));
		dateModifiedView.setText(formatDate(note.getDateModified()));
		characterCountView.setText(String.format(Locale.getDefault(), "%d", note.getCharacterCount()));
		wordCountView.setText(String.format(Locale.getDefault(), "%d", note.getWordCount()));
		paragraphCountView.setText(String.format(Locale.getDefault(), "%d", note.getParagraphCount()));
		readTimeView.setText(formatTime(note.getReadTime()));

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		return v;
	}

	//format time in: Xh Xm Xs
	private String formatTime(int timeInSeconds) {
		int hours = timeInSeconds / 3600;
		int minutes = (timeInSeconds % 3600) / 60;
		int seconds = timeInSeconds % 3600 % 60;
		if (hours != 0) return hours + "h " + minutes + "m " + seconds + "s";
		if (minutes != 0) return minutes + "m " + seconds + "s";
		return seconds + "s";
	}

	//format date in: dd MM yyyy 'at' hh:mm:ss a
	private String formatDate(String dateTime) {
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateTime;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy 'at' hh:mm:ss a", Locale.getDefault());
		if (date == null) return dateTime;

		String fDate = dateFormat.format(date);
		int month = Integer.parseInt(fDate.substring(3, 5));
		fDate = fDate.substring(0, 3) + new DateFormatSymbols().getMonths()[month - 1] + fDate.substring(5);
		return fDate;
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		note = ((EditorActivity)context).getNote();
	}
}
