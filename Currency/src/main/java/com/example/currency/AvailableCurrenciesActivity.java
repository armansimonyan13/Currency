package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
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

		getActionBar().setDisplayHomeAsUpEnabled(true);

		availableCurrenciesDbAdapter = new AvailableCurrenciesDbAdapter(this);
		selectedCurrenciesDbAdapter = new SelectedCurrenciesDbAdapter(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			selectedCurrenciesDbAdapter.open();
			availableCurrenciesDbAdapter.open();
		} catch (SQLiteException e) {
			e.printStackTrace();
			finish();
		}
		setListAdapter(new AvailableCurrenciesCursorAdapter(
				this, availableCurrenciesDbAdapter, selectedCurrenciesDbAdapter));
	}

	@Override
	protected void onPause() {
		availableCurrenciesDbAdapter.close();
		selectedCurrenciesDbAdapter.close();

		super.onPause();
	}
}
