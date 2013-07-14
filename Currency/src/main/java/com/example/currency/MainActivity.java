package com.example.currency;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ListActivity implements PullToRefreshAttacher.OnRefreshListener {
	public static final String URL_DOMAIN = "http://openexchangerates.org";
	public static final String URL_CURRENCIES = "/currencies.json";
	public static final String URL_API_LATEST = "/api/latest.json";
	public static final String URL_API_HISTORICAL = "/api/historical/YYYY-MM-DD.json";
	public static final String API_ID_KEY = "app_id";
	public static final String API_ID_VALUE = "c93129b770a04ee5980efe192124f949";

	private Latest latest;
	private PullToRefreshAttacher pullToRefreshAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

	public void updateRates() {
		new AsyncTask<Void, Void, Latest>() {
			@Override
			protected Latest doInBackground(Void... dummy) {
				Latest latest = null;
				HttpURLConnection urlConnection = null;

				try {
					URL url = new URL(URL_DOMAIN + URL_API_LATEST + "?" + API_ID_KEY + "=" + API_ID_VALUE);
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

				BaseAdapter adapter = new LatestAdapter(MainActivity.this, latest);

				MainActivity.this.setListAdapter(adapter);

				pullToRefreshAttacher.setRefreshComplete();
			}
		}.execute();
	}

	@Override
	public void onRefreshStarted(View view) {
		updateRates();
	}
}
