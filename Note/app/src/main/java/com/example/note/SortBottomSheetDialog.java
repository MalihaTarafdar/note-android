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

		sortData = new HashMap<>();

		List<SortItem> items = new ArrayList<SortItem>() {{
			add(new SortItem("Title", R.drawable.ic_title));
			add(new SortItem("Date Created", R.drawable.ic_date_range));
			add(new SortItem("Date Modified", R.drawable.ic_date_range));
			add(new SortItem("Word Count", R.drawable.ic_text));
			add(new SortItem("Read Time", R.drawable.ic_time));
			add(new SortItem("Paragraph Count", R.drawable.ic_text));
			add(new SortItem("Character Count", R.drawable.ic_text));
		}};

		//show current sort data
		if (getFragmentManager() != null) {
			NotesFragment notesFragment = (NotesFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
			if (notesFragment != null) {
				List<DatabaseHelper.SortData> sortByList = notesFragment.getSortByList();
				for (DatabaseHelper.SortData sd : sortByList) {
					for (SortItem item : items) {
						if (sd.getCol().equals(item.name.replace(" ", ""))) {
							item.selected = true;
							item.asc = sd.isAscending();
							sortData.put(sd.getCol(), sd.isAscending());
						}
					}
				}
			}
		}

		//list view
		ListView listView = v.findViewById(R.id.sort_lv_options);
		CustomAdapter customAdapter = new CustomAdapter(v.getContext(), R.layout.item_sort_filter, items);
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
		public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.item_sort_filter, null);

			LinearLayout layout = v.findViewById(R.id.sort_filter_item_layout);
			final ImageView icon = v.findViewById(R.id.sort_filter_item_iv_icon);
			final TextView nameView = v.findViewById(R.id.sort_filter_item_name);
			final Switch ascSwitch = v.findViewById(R.id.sort_filter_item_sw_asc);

			icon.setImageResource(list.get(position).iconId);
			nameView.setText(list.get(position).name);
			ascSwitch.setChecked(list.get(position).asc);

			if (list.get(position).selected) {
				icon.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary));
				nameView.setTextColor(getResources().getColor(R.color.colorPrimary));
				ascSwitch.setEnabled(true);
				try {
					sortData.put(nameView.getText().toString().replace(" ", ""), ascSwitch.isChecked());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else {
				icon.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
				nameView.setTextColor(getResources().getColor(R.color.colorText));
				ascSwitch.setEnabled(false);
				try {
					sortData.remove(nameView.getText().toString().replace(" ", ""));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					list.get(position).selected = !list.get(position).selected;
					notifyDataSetChanged();
				}
			});

			ascSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					try {
						sortData.put(nameView.getText().toString().replace(" ", ""), isChecked);
						list.get(position).asc = isChecked;
						notifyDataSetChanged();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			});

			return v;
		}
	}

	private static class SortItem {
		String name;
		int iconId;
		boolean selected, asc;
		SortItem(String name, int iconId) {
			this.name = name;
			this.iconId = iconId;
			selected = false;
			asc = false;
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
