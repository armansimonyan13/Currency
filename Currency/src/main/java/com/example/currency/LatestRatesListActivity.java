package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.net.HttpURLConnection;
import java.net.URL;

public class LatestRatesListActivity extends ListActivity implements PullToRefreshAttacher.OnRefreshListener {
	private Latest latest;
	private PullToRefreshAttacher pullToRefreshAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_rates_list);

	    if (savedInstanceState == null) {
		    updateRates();
	    }

	    pullToRefreshAttacher = new PullToRefreshAttacher(this);
	    pullToRefreshAttacher.setRefreshableView(getListView(), this);
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
	public void onRefreshStarted(View view) {
		updateRates();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent converterActivityIntent = new Intent(this, ConverterActivity.class);
		TextView valueTextView = (TextView) v.findViewById(R.id.value);
		converterActivityIntent.putExtra(ConverterActivity.TO_VALUE, Double.parseDouble(valueTextView.getText().toString()));


		startActivity(converterActivityIntent);
	}

	private void updateRates() {
		new AsyncTask<Void, Void, Latest>() {
			@Override
			protected Latest doInBackground(Void... dummy) {
				Latest latest = null;
				HttpURLConnection urlConnection = null;

				try {
					URL url = new URL(Constants.URL_DOMAIN + Constants.URL_API_LATEST + "?"
							+ Constants.API_ID_KEY + "=" + Constants.API_ID_VALUE);
					urlConnection = (HttpURLConnection) url.openConnection();
					LatestReader latestReader = new LatestReader(urlConnection.getInputStream());
					latest = latestReader.read();
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
}
