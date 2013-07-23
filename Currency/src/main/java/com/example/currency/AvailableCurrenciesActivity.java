package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public class AvailableCurrenciesActivity extends ListActivity {

	private CurrenciesDAO currenciesDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_available_currencies);

		currenciesDAO = new CurrenciesDAO(this);
		setListAdapter(new AvailableCurrenciesAdapter(this, currenciesDAO));
	}

	@Override
	protected void onResume() {
		super.onResume();

		currenciesDAO.open();
	}

	@Override
	protected void onPause() {
		super.onPause();

		currenciesDAO.close();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
