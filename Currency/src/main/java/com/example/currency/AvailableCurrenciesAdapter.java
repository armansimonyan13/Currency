package com.example.currency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AvailableCurrenciesAdapter extends BaseAdapter {
	private Context context;
	private String[] availableCurrencies;
	private LayoutInflater inflater;

	public AvailableCurrenciesAdapter(Context context) {
		this.context = context;
		availableCurrencies = context.getResources().getStringArray(R.array.available_currencies);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public int getCount() {
		return availableCurrencies.length;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		View view;

		if (convertView == null) {
			view = inflater.inflate(R.layout.view_item_available_currencies, viewGroup, false);
		} else {
			view = convertView;
		}

		String currency = availableCurrencies[i];


		((TextView) view.findViewById(R.id.name)).setText(currency.toUpperCase());

		ImageView flagImage = (ImageView) view.findViewById(R.id.flag);

		flagImage.setTag(currency);
		int imageId = context.getResources().getIdentifier("flag_" + currency, "drawable", context.getPackageName());

		if (imageId == 0) {
			flagImage.setImageResource(R.drawable.flag_not_available);
		} else {
			flagImage.setImageResource(imageId);
		}

		int stringId = context.getResources().getIdentifier("currency_" + currency, "string", context.getPackageName());
		((TextView) view.findViewById(R.id.description)).setText(" - " + context.getResources().getString(stringId));

		return view;
	}
}