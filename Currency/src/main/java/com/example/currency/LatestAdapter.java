package com.example.currency;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LatestAdapter extends BaseAdapter {
	private LatestRatesListActivity activity;
	private Latest latest;
	private LayoutInflater inflater;

	public LatestAdapter(LatestRatesListActivity activity, Latest latest) {
		this.activity = activity;
		this.latest = latest;
		this.inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return latest.getRates().size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		View view;

		if (convertView == null) {
			view = inflater.inflate(R.layout.view_item_rate, viewGroup, false);
		} else {
			view = convertView;
		}

		Pair<String, Double> pair = latest.getRates().get(i);

		((TextView) view.findViewById(R.id.name)).setText(pair.first);
		((TextView) view.findViewById(R.id.value)).setText(pair.second.toString());

		ImageView flagImage = (ImageView) view.findViewById(R.id.flag);

		String flagName = pair.first.toLowerCase();
		flagImage.setTag(flagName);
		int imageId = activity.getResources().getIdentifier("flag_" + flagName, "drawable", activity.getPackageName());

		if (imageId == 0) {
			flagImage.setImageResource(R.drawable.flag_not_available);
		} else {
			flagImage.setImageResource(imageId);
		}

		int stringId = activity.getResources().getIdentifier("currency_" + flagName, "string", activity.getPackageName());
		((TextView) view.findViewById(R.id.description)).setText(" - " + activity.getResources().getString(stringId));

		return view;
	}
}
