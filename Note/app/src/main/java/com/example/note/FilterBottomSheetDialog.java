package com.example.note;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

class FilterBottomSheetDialog extends BottomSheetDialogFragment {

	private FilterListener listener;
	private DatabaseHelper.FilterData oldFilterData, filterData;
	private boolean editing;

	FilterBottomSheetDialog(DatabaseHelper.FilterData filterData) {
		oldFilterData = filterData;
		this.filterData = filterData;
		editing = true;
	}
	FilterBottomSheetDialog() {
		editing = false;
		filterData = null;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

		final TextInputEditText startInput = v.findViewById(R.id.filter_input_start);
		final TextInputEditText endInput = v.findViewById(R.id.filter_input_end);
		Button addButton = v.findViewById(R.id.filter_btn_add);
		ImageButton closeButton = v.findViewById(R.id.filter_btn_close);

		List<FilterItem> items = new ArrayList<FilterItem>() {{
			add(new FilterItem("Title", R.drawable.ic_title));
			add(new FilterItem("Date Created", R.drawable.ic_date_range));
			add(new FilterItem("Date Modified", R.drawable.ic_date_range));
			add(new FilterItem("Word Count", R.drawable.ic_text));
			add(new FilterItem("Read Time", R.drawable.ic_time));
			add(new FilterItem("Paragraph Count", R.drawable.ic_text));
			add(new FilterItem("Character Count", R.drawable.ic_text));
		}};

		if (filterData != null) {
			addButton.setText(R.string.save);
			startInput.setText(filterData.getLowerBound().replaceAll("'", ""));
			endInput.setText(filterData.getUpperBound().replaceAll("'", ""));
			for (FilterItem item : items) {
				if (filterData.getCol().equals(item.name.replaceAll(" ", ""))) {
					item.selected = true;
					break;
				}
			}
		} else {
			filterData = new DatabaseHelper.FilterData(null, "", "");
		}

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filterData.getCol() == null) {
					Toast.makeText(getContext(), "Please select a filter option", Toast.LENGTH_SHORT).show();
					return;
				}

				String lowerBound = (startInput.getText() != null) ? startInput.getText().toString() : "";
				String upperBound = (endInput.getText() != null) ? endInput.getText().toString() : "";
				if (lowerBound.isEmpty() && upperBound.isEmpty()) {
					Toast.makeText(getContext(), "Please fill out the start and/or end fields", Toast.LENGTH_SHORT).show();
					return;
				}

				String col = filterData.getCol();
				//put quotes around bounds that will be entered as strings
				if (col.equals(getString(R.string.title).replaceAll(" ", "")) ||
						col.equals(getString(R.string.date_created).replaceAll(" ", "")) ||
						col.equals(getString(R.string.date_modified).replaceAll(" ", ""))) {
					lowerBound = "'" + lowerBound + "'";
					upperBound = "'" + upperBound + "'";
				} else { //bounds should be integers
					try {
						if (!lowerBound.isEmpty()) Integer.parseInt(lowerBound);
						if (!upperBound.isEmpty()) Integer.parseInt(upperBound);
					} catch (NumberFormatException e) {
						Toast.makeText(v.getContext(), "For the selected filter, only whole numbers can be used", Toast.LENGTH_SHORT).show();
						return;
					}
				}

				filterData.setLowerBound(lowerBound);
				filterData.setUpperBound(upperBound);
				filterData.setCol(col.replaceAll(" ", ""));
				if (editing) listener.onFilterSaved(oldFilterData, filterData);
				else listener.onFilterAdded(filterData);
				dismiss();
			}
		});

		ListView listView = v.findViewById(R.id.filter_lv_options);
		CustomAdapter customAdapter = new CustomAdapter(v.getContext(), items);
		listView.setAdapter(customAdapter);

		return v;
	}
	
	private class CustomAdapter extends ArrayAdapter<FilterItem> {
		private Context context;
		private List<FilterItem> list;

		CustomAdapter(@NonNull Context context, @NonNull List<FilterItem> objects) {
			super(context, R.layout.item_sort_filter, objects);
			this.context = context;
			list = objects;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.item_sort_filter, null);

			LinearLayout layout = v.findViewById(R.id.sort_filter_item_layout);
			ImageView icon = v.findViewById(R.id.sort_filter_item_iv_icon);
			final TextView nameView = v.findViewById(R.id.sort_filter_item_name);
			Switch ascSwitch = v.findViewById(R.id.sort_filter_item_sw_asc);
			TextView swView = v.findViewById(R.id.sort_filter_item_tv_asc);

			ascSwitch.setVisibility(View.GONE);
			swView.setVisibility(View.GONE);
			icon.setImageResource(list.get(position).iconId);
			nameView.setText(list.get(position).name);

			if (list.get(position).selected) {
				nameView.setTextColor(getResources().getColor(R.color.colorPrimary));
				icon.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary));
			}

			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					filterData.setCol(nameView.getText().toString().replaceAll(" ", ""));
					for (FilterItem item : list) {
						item.selected = false;
					}
					list.get(position).selected = true;
					notifyDataSetChanged();
				}
			});

			return v;
		}
	}

	private static class FilterItem {
		String name;
		int iconId;
		boolean selected;
		FilterItem(String name, int iconId) {
			this.name = name;
			this.iconId = iconId;
			selected = false;
		}
	}

	interface FilterListener {
		void onFilterAdded(DatabaseHelper.FilterData filterData);
		void onFilterSaved(DatabaseHelper.FilterData oldFilterData, DatabaseHelper.FilterData filterData);
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
