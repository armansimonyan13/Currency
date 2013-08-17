package com.example.currency;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedCurrenciesCursorAdapter extends CursorAdapter {
	private LayoutInflater layoutInflater;
	private SelectedCurrenciesListActivity activity;
	private OnCurrencyDragEventListener currencyDragEventListener = new OnCurrencyDragEventListener() {
		@Override
		public void replace(String name1, String name2) {
//			activity.replace(name1, name2);
		}
	};

	public SelectedCurrenciesCursorAdapter(SelectedCurrenciesListActivity context, Cursor cursor, int flag) {
		super(context, cursor, flag);
		this.activity = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String name = cursor.getString(cursor.getColumnIndex(AvailableCurrenciesSQLiteHelper.COLUMN_NAME));
		double value = cursor.getDouble(cursor.getColumnIndex(AvailableCurrenciesSQLiteHelper.COLUMN_VALUE));
		((TextView) view.findViewById(R.id.name)).setText(name);
		((TextView) view.findViewById(R.id.value)).setText(String.valueOf(value));

		view.setTag(name);

		/*
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				String text = (String) view.getTag();
				ClipData.Item item = new ClipData.Item(text);
				ClipData clipData = new ClipData(text, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
				View.DragShadowBuilder dragShadowBuilder = new CurrencyDragShadowBuilder(view);

				view.startDrag(clipData, dragShadowBuilder, null, 0);
				view.setAlpha(0.0f);
				return false;
			}
		});
		*/

		view.setOnDragListener(currencyDragEventListener);

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

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return layoutInflater.inflate(R.layout.view_item_rate, viewGroup, false);
	}
}