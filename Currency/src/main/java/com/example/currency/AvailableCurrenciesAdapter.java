package com.example.currency;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class AvailableCurrenciesAdapter extends CursorAdapter {
	private static final String TAG = AvailableCurrenciesAdapter.class.getName();

	private Context context;
	private LayoutInflater inflater;
	private SelectedCurrenciesDbAdapter selectedCurrenciesDAO;

	public AvailableCurrenciesAdapter(Context context, Cursor cursor, SelectedCurrenciesDbAdapter selectedCurrenciesDAO) {
		super(context, cursor, 0);
		this.context = context;
		this.selectedCurrenciesDAO = selectedCurrenciesDAO;
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

		String currencyName = cursor.getString(
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

		if (selectedCurrenciesDAO.contains(cursor.getInt(0))) {
			((CheckBox) view.findViewById(R.id.selection)).setChecked(true);
		}
	}
}
