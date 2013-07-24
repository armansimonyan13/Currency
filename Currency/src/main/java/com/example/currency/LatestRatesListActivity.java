package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LatestRatesListActivity extends ListActivity implements PullToRefreshAttacher.OnRefreshListener {
	private static final String TAG = LatestRatesListActivity.class.getName();

	private PullToRefreshAttacher pullToRefreshAttacher;
	private CurrenciesDAO currenciesDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_rates_list);

	    if (savedInstanceState == null) {
		    updateRates();
	    }

	    pullToRefreshAttacher = new PullToRefreshAttacher(this);
	    pullToRefreshAttacher.setRefreshableView(getListView(), this);

	    currenciesDAO = new CurrenciesDAO(this);
    }

	@Override
	protected void onResume() {
		super.onResume();

		try {
			currenciesDAO.open();

			if (currenciesDAO.getAllCurrencies().isEmpty()) {
				for (String currencyName : getResources().getStringArray(R.array.initial_currencies)) {
					currenciesDAO.createCurrency(currencyName, 0.0);
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		currenciesDAO.close();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean("filled", true);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
			case R.id.action_settings:
				intent = new Intent(LatestRatesListActivity.this, AvailableCurrenciesActivity.class);
				startActivityForResult(intent, 13);
				return true;
			case R.id.action_about:
				intent = new Intent(LatestRatesListActivity.this, AboutActivity.class);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefreshStarted(View view) {
		updateRates();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent converterActivityIntent = new Intent(this, ConverterActivity.class);

		TextView valueTextView = (TextView) v.findViewById(R.id.value);
		converterActivityIntent.putExtra(ConverterActivity.TO_VALUE,
				Double.parseDouble(valueTextView.getText().toString()));

		ImageView toFlagImageView = (ImageView) v.findViewById(R.id.flag);
		String currencyName = (String) toFlagImageView.getTag();
		converterActivityIntent.putExtra(ConverterActivity.TO_FLAG, currencyName);

		startActivity(converterActivityIntent);
	}

	private void updateRates() {
		new AsyncTask<Void, Void, Latest>() {
			@Override
			protected Latest doInBackground(Void... voids) {
				Latest latest = null;
				HttpURLConnection urlConnection = null;

				try {
					URL url = new URL(Constants.URL_DOMAIN + Constants.URL_API_LATEST + "?"
							+ Constants.API_ID_KEY + "=" + Constants.API_ID_VALUE);
					urlConnection = (HttpURLConnection) url.openConnection();
					LatestReader latestReader = new LatestReader(urlConnection.getInputStream());
					latest = latestReader.read();

					List<Pair<String, Double>> allRates = latest.getRates();
					List<Pair<String, Double>> filteredRates = new ArrayList<Pair<String, Double>>();

					for (Currency currency : currenciesDAO.getAllCurrencies()) {
						String name = currency.getName();

						for (Pair<String, Double> rate : allRates) {
							if (name.equals(rate.first)) {
								filteredRates.add(rate);
								break;
							}
						}
					}

					for (Pair<String, Double> rate : filteredRates) {
						currenciesDAO.updateCurrency(rate.first, rate.second);
					}

					latest.setRates(filteredRates);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					urlConnection.disconnect();
				}

				return latest;
			}

			@Override
			protected void onPostExecute(Latest latest) {
				if (latest == null) {
					return;
				}

				BaseAdapter adapter = new LatestAdapter(LatestRatesListActivity.this, latest);
				LatestRatesListActivity.this.setListAdapter(adapter);

				pullToRefreshAttacher.setRefreshComplete();
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 13) {
				updateRates();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void replace(String name1, String name2) {
		Currency currency1 = currenciesDAO.getCurrency(name1);
		Currency currency2 = currenciesDAO.getCurrency(name2);
		currenciesDAO.updateCurrency(currency1.getId(), currency2.getName(), currency2.getValue());
		currenciesDAO.updateCurrency(currency2.getId(), currency1.getName(), currency1.getValue());
	}
}
