package com.example.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SortBottomSheetDialog extends BottomSheetDialogFragment {

	private SortListener listener;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_sort, container, false);

		ImageButton closeButton = v.findViewById(R.id.sort_btn_close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		ListView listView = v.findViewById(R.id.sort_lv_options);
		List<SortItem> items = new ArrayList<SortItem>() {{
			add(new SortItem("Title", R.drawable.ic_title));
			add(new SortItem("Date Created", R.drawable.ic_date_range));
			add(new SortItem("Date Modified", R.drawable.ic_date_range));
			add(new SortItem("Word Count", R.drawable.ic_text));
			add(new SortItem("Read Time", R.drawable.ic_time));
			add(new SortItem("Paragraph Count", R.drawable.ic_text));
			add(new SortItem("Character Count", R.drawable.ic_text));
		}};
		CustomAdapter customAdapter = new CustomAdapter(v.getContext(), R.layout.item_sort, items);
		listView.setAdapter(customAdapter);

		return v;
	}

	private static class CustomAdapter extends ArrayAdapter<SortItem> {
		private Context context;
		private List<SortItem> list;
		CustomAdapter(@NonNull Context context, int resource, @NonNull List<SortItem> objects) {
			super(context, resource, objects);
			this.context = context;
			list = objects;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.item_sort, null);

			ImageView icon = v.findViewById(R.id.sort_item_iv_icon);
			TextView nameView = v.findViewById(R.id.sort_item_name);

			icon.setImageResource(list.get(position).iconId);
			nameView.setText(list.get(position).name);

			return v;
		}
	}

	private static class SortItem {
		String name;
		int iconId;
		SortItem(String name, int iconId) {
			this.name = name;
			this.iconId = iconId;
		}
	}

	interface SortListener {
		void onSortApplied(List<DatabaseHelper.SortData> sortByList);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (SortListener)context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement SortListener");
		}
	}
}
