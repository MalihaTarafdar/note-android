package com.example.note;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortBottomSheetDialog extends BottomSheetDialogFragment {

	private SortListener listener;
	private HashMap<String, Boolean> sortData;

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

		List<SortItem> items = new ArrayList<SortItem>() {{
			add(new SortItem("Title", R.drawable.ic_title));
			add(new SortItem("Date Created", R.drawable.ic_date_range));
			add(new SortItem("Date Modified", R.drawable.ic_date_range));
			add(new SortItem("Word Count", R.drawable.ic_text));
			add(new SortItem("Read Time", R.drawable.ic_time));
			add(new SortItem("Paragraph Count", R.drawable.ic_text));
			add(new SortItem("Character Count", R.drawable.ic_text));
		}};

		sortData = new HashMap<>();

		ListView listView = v.findViewById(R.id.sort_lv_options);
		CustomAdapter customAdapter = new CustomAdapter(v.getContext(), R.layout.item_sort, items);
		listView.setAdapter(customAdapter);

		Button applyButton = v.findViewById(R.id.sort_btn_apply);
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<DatabaseHelper.SortData> list = new ArrayList<>();
				for (Map.Entry<String, Boolean> entry : sortData.entrySet()) {
					list.add(new DatabaseHelper.SortData(entry.getKey(), entry.getValue()));
				}
				listener.onSortApplied(list);
				dismiss();
			}
		});

		return v;
	}

	private class CustomAdapter extends ArrayAdapter<SortItem> {
		private Context context;
		private List<SortItem> list;

		CustomAdapter(@NonNull Context context, int resource, @NonNull List<SortItem> objects) {
			super(context, resource, objects);
			this.context = context;
			list = objects;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.item_sort, null);

			LinearLayout layout = v.findViewById(R.id.sort_item_layout);
			final ImageView icon = v.findViewById(R.id.sort_item_iv_icon);
			final TextView nameView = v.findViewById(R.id.sort_item_name);
			final Switch ascSwitch = v.findViewById(R.id.sort_item_sw_asc);

			icon.setImageResource(list.get(position).iconId);
			nameView.setText(list.get(position).name);

			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (nameView.getCurrentTextColor() != getResources().getColor(R.color.colorPrimary)) {
						icon.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary));
						nameView.setTextColor(getResources().getColor(R.color.colorPrimary));
						ascSwitch.setEnabled(true);
						try {
							sortData.put(getColName(nameView.getText().toString()), ascSwitch.isChecked());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					} else {
						icon.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
						nameView.setTextColor(getResources().getColor(R.color.colorText));
						ascSwitch.setEnabled(false);
						try {
							sortData.remove(getColName(nameView.getText().toString()));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			});

			ascSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					try {
						sortData.put(getColName(nameView.getText().toString()), isChecked);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			});

			return v;
		}
	}

	private String getColName(String name) throws IllegalArgumentException {
		if (name.equals(getString(R.string.title))) {
			return DatabaseHelper.COL_TITLE;
		} else if (name.equals(getString(R.string.date_created))) {
			return DatabaseHelper.COL_DATE_CREATED;
		} else if (name.equals(getString(R.string.date_modified))) {
			return DatabaseHelper.COL_DATE_MODIFIED;
		} else if (name.equals(getString(R.string.character_count))) {
			return DatabaseHelper.COL_CHARACTER_COUNT;
		} else if (name.equals(getString(R.string.word_count))) {
			return DatabaseHelper.COL_WORD_COUNT;
		} else if (name.equals(getString(R.string.paragraph_count))) {
			return DatabaseHelper.COL_PARAGRAPH_COUNT;
		} else if (name.equals(getString(R.string.read_time))) {
			return DatabaseHelper.COL_READ_TIME;
		}
		throw new IllegalArgumentException("Provided string does not match any column name");
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
