package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.currency.CursorAdapter.AvailableCurrenciesCursorAdapter;
import com.example.currency.DbAdapter.AvailableCurrenciesDbAdapter;
import com.example.currency.DbAdapter.SelectedCurrenciesDbAdapter;

public class AvailableCurrenciesActivity extends ListActivity {

	private AvailableCurrenciesDbAdapter availableCurrenciesDbAdapter;
	private SelectedCurrenciesDbAdapter selectedCurrenciesDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_available_currencies);

		availableCurrenciesDbAdapter = new AvailableCurrenciesDbAdapter(this);
		selectedCurrenciesDbAdapter = new SelectedCurrenciesDbAdapter(this);

		availableCurrenciesDbAdapter.open();
		selectedCurrenciesDbAdapter.open();

		setListAdapter(new AvailableCurrenciesCursorAdapter(
				this, availableCurrenciesDbAdapter, selectedCurrenciesDbAdapter));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {

		availableCurrenciesDbAdapter.close();
		selectedCurrenciesDbAdapter.close();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
