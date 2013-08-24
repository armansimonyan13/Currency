package com.example.currency.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.currency.DbAdapter.AvailableCurrenciesDbAdapter;
import com.example.currency.DbAdapter.SelectedCurrenciesDbAdapter;
import com.example.currency.R;

public class AvailableCurrenciesCursorAdapter extends CursorAdapter {
	private static final String TAG = AvailableCurrenciesCursorAdapter.class.getName();

	private Context context;
	private LayoutInflater inflater;
	private SelectedCurrenciesDbAdapter selectedCurrenciesDbAdapter;

	private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
			new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					String currencyName = (String) compoundButton.getTag();

					if (checked) {
						selectedCurrenciesDbAdapter.insert(currencyName);
					} else {
						selectedCurrenciesDbAdapter.delete(currencyName);
					}

				}
			};

	public AvailableCurrenciesCursorAdapter(
			Context context,
			AvailableCurrenciesDbAdapter availableCurrenciesDbAdapter,
			SelectedCurrenciesDbAdapter selectedCurrenciesDbAdapter) {
		super(context, availableCurrenciesDbAdapter.fetchAll(), 0);
		this.context = context;
		this.selectedCurrenciesDbAdapter = selectedCurrenciesDbAdapter;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return inflater.inflate(R.layout.view_item_available_currencies, viewGroup, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView description = (TextView) view.findViewById(R.id.description);
		ImageView flagImage = (ImageView) view.findViewById(R.id.flag);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.selection);
		final String currencyName = cursor.getString(
				cursor.getColumnIndex(AvailableCurrenciesDbAdapter.COLUMN_NAME));

		name.setText(currencyName);
		description.setText(cursor.getString(
				cursor.getColumnIndex(AvailableCurrenciesDbAdapter.COLUMN_VALUE)));

		int flagResourceId = context.getResources().getIdentifier(
				"flag_" + currencyName.toLowerCase(), "drawable", context.getPackageName());

		if (flagResourceId == 0) {
			flagImage.setImageResource(R.drawable.flag_not_available);
		} else {
			flagImage.setImageResource(flagResourceId);
		}

		checkBox.setOnCheckedChangeListener(null);

		if (selectedCurrenciesDbAdapter.contains(
				cursor.getString(
						cursor.getColumnIndex(AvailableCurrenciesDbAdapter.COLUMN_NAME)))) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}

		checkBox.setTag(currencyName);

		checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
	}
}
