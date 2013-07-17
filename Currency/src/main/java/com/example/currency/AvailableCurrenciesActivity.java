package com.example.currency;

import android.app.ListActivity;
import android.os.Bundle;

public class AvailableCurrenciesActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_available_currencies);

		setListAdapter(new AvailableCurrenciesAdapter(this));
	}
}
