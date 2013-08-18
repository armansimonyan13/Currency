package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public class AvailableCurrenciesActivity extends ListActivity {

	private AvailableCurrenciesDbAdapter availableCurrenciesDbAdapter;
	private SelectedCurrenciesDbAdapter selectedCurrenciesDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_available_currencies);

		availableCurrenciesDbAdapter = new AvailableCurrenciesDbAdapter(this);
		selectedCurrenciesDbAdapter = new SelectedCurrenciesDbAdapter(this);
		setListAdapter(new AvailableCurrenciesAdapter(
				this, availableCurrenciesDbAdapter.fetchAll(), selectedCurrenciesDbAdapter));
	}

	@Override
	protected void onResume() {
		super.onResume();

		availableCurrenciesDbAdapter.open();
		selectedCurrenciesDbAdapter.open();
	}

	@Override
	protected void onPause() {
		super.onPause();

		availableCurrenciesDbAdapter.close();
		selectedCurrenciesDbAdapter.close();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
