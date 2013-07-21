package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.*;

public class AvailableCurrenciesActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_available_currencies);

		setListAdapter(new AvailableCurrenciesAdapter(this));
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent().putExtra("selecteds", ((AvailableCurrenciesAdapter) getListAdapter()).getSelecteds());
		setResult(RESULT_OK, intent);
		setPreferredCurrencies();
		finish();
	}

	public void setPreferredCurrencies() {
		ArrayList<String> selecteds = ((AvailableCurrenciesAdapter) getListAdapter()).getSelecteds();
		SharedPreferences sharedPreferences = getSharedPreferences("application_settings", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putStringSet("preferred_currencies", new HashSet<String>(selecteds));
		editor.commit();
	}

	public ArrayList<String> getPreferedCurrencies() {
		SharedPreferences sharedPreferences = getSharedPreferences("application_settings", MODE_PRIVATE);
		Set<String> preferredCurrencies = sharedPreferences.getStringSet("preferred_currencies", null);

		if (preferredCurrencies == null) {
			return new ArrayList<String>(
					Arrays.asList(getResources().getStringArray(R.array.initial_currencies)));
		} else {
			return new ArrayList<String>(preferredCurrencies);
		}
	}
}
