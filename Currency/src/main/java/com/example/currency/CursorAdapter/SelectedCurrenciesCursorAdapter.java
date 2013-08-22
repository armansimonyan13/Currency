package com.example.currency.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.currency.DbAdapter.AvailableCurrenciesDbAdapter;
import com.example.currency.R;
import com.example.currency.SelectedCurrenciesListActivity;

public class SelectedCurrenciesCursorAdapter extends CursorAdapter {
	private LayoutInflater layoutInflater;
	private Context context;

	public SelectedCurrenciesCursorAdapter(
			SelectedCurrenciesListActivity context, Cursor cursor, int flag) {
		super(context, cursor, flag);
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return layoutInflater.inflate(R.layout.view_item_selected_currency, viewGroup, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String name = cursor.getString(cursor.getColumnIndex(AvailableCurrenciesDbAdapter.COLUMN_NAME));
		double value = cursor.getDouble(cursor.getColumnIndex(AvailableCurrenciesDbAdapter.COLUMN_VALUE));
		((TextView) view.findViewById(R.id.name)).setText(name);
		((TextView) view.findViewById(R.id.value)).setText(String.valueOf(value));

		view.setTag(name);

		ImageView flagImage = (ImageView) view.findViewById(R.id.flag);

		String flagName = name.toLowerCase();
		flagImage.setTag(flagName);
		int imageId = context.getResources().getIdentifier("flag_" + flagName, "drawable", context.getPackageName());

		if (imageId == 0) {
			flagImage.setImageResource(R.drawable.flag_not_available);
		} else {
			flagImage.setImageResource(imageId);
		}

		int stringId = context.getResources().getIdentifier("currency_" + flagName, "string", context.getPackageName());
		((TextView) view.findViewById(R.id.description)).setText(" - " + context.getResources().getString(stringId));
	}
}
