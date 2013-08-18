package com.example.currency;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SelectedCurrenciesListActivity extends ListActivity implements PullToRefreshAttacher.OnRefreshListener {
	private static final String TAG = SelectedCurrenciesListActivity.class.getName();

	private PullToRefreshAttacher pullToRefreshAttacher;

	private SelectedCurrenciesDbAdapter selectedCurrenciesDbAdapter;
	private SelectedCurrenciesCursorAdapter selectedCurrenciesCursorAdapter;

	private AvailableCurrenciesDbAdapter availableCurrenciesDbAdapter;

	private DragSortListView  dslv;
	private DragSortController controller;

	public int dragStartMode = DragSortController.ON_DOWN;
	public int removeMode = DragSortController.FLING_REMOVE;

	public boolean removeEnabled = false;
	public boolean sortEnabled = true;
	public boolean dragEnabled = true;


	private DragSortListView.DropListener dropListener =
			new DragSortListView.DropListener() {
				@Override
				public void drop(int from, int to) {
					if (from != to) {
						Log.d("+++++", "Dropped");
						Log.d("+++++", "" + from);
						Log.d("+++++", "" + to);

						selectedCurrenciesDbAdapter.replace(from, to);
						((SelectedCurrenciesCursorAdapter) getListAdapter()).notifyDataSetChanged();
					}
				}
			};

	private DragSortListView.RemoveListener removeListener =
			new DragSortListView.RemoveListener() {
				@Override
				public void remove(int which) {
					selectedCurrenciesDbAdapter.delete(which);
				}
			};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_rates_list);

//	    if (savedInstanceState == null) {
//		    updateRates();
//	    }

	    dslv = (DragSortListView) getListView();

	    controller = buildController(dslv);
	    dslv.setFloatViewManager(controller);
	    dslv.setOnTouchListener(controller);
	    dslv.setDragEnabled(dragEnabled);

	    dslv.setDropListener(dropListener);
	    dslv.setRemoveListener(removeListener);

//	    pullToRefreshAttacher = new PullToRefreshAttacher(this);
//	    pullToRefreshAttacher.setRefreshableView(getListView(), this);

	    selectedCurrenciesDbAdapter = new SelectedCurrenciesDbAdapter(this);
	    availableCurrenciesDbAdapter = new AvailableCurrenciesDbAdapter(this);
    }

	@Override
	protected void onResume() {
		super.onResume();

		try {
			selectedCurrenciesDbAdapter.open();
			availableCurrenciesDbAdapter.open();

			if (selectedCurrenciesDbAdapter.isEmpty()) {
				for (String currencyName : getResources().getStringArray(R.array.initial_currencies)) {
					// TODO: check if cursor valid
					CurrencyData currencyData = AvailableCurrenciesDbAdapter.cursorToCurrency(
							availableCurrenciesDbAdapter.fetch(currencyName));

					selectedCurrenciesDbAdapter.insert(currencyData.getId());
				}
			}

			selectedCurrenciesCursorAdapter = new SelectedCurrenciesCursorAdapter(
					SelectedCurrenciesListActivity.this,
					selectedCurrenciesDbAdapter.fetchAll(), 0);

			setListAdapter(selectedCurrenciesCursorAdapter);
		} catch (SQLiteException e) {
			e.printStackTrace();
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		selectedCurrenciesDbAdapter.close();
		availableCurrenciesDbAdapter.close();
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
				intent = new Intent(SelectedCurrenciesListActivity.this, AvailableCurrenciesActivity.class);
				startActivityForResult(intent, 13);
				return true;
			case R.id.action_about:
				intent = new Intent(SelectedCurrenciesListActivity.this, AboutActivity.class);
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
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				Latest latest = null;
				HttpURLConnection urlConnection = null;

				try {
					URL url = new URL(Constants.URL_DOMAIN + Constants.URL_API_LATEST + "?"
							+ Constants.API_ID_KEY + "=" + Constants.API_ID_VALUE);
					urlConnection = (HttpURLConnection) url.openConnection();
					LatestReader latestReader = new LatestReader(urlConnection.getInputStream());
					latest = latestReader.read();

					List<Pair<String, Double>> allRates = latest.getRates();
					availableCurrenciesDbAdapter.deleteAll();
					for (Pair<String, Double> rate : allRates) {
						availableCurrenciesDbAdapter.create(rate.first, rate.second);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					urlConnection.disconnect();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void voids) {
				((CursorAdapter) getListAdapter()).changeCursor(
						selectedCurrenciesDbAdapter.fetchAll());

//				pullToRefreshAttacher.setRefreshComplete();
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

	public DragSortController buildController(DragSortListView dslv) {
		DragSortController controller = new DragSortController(dslv);
		controller.setDragHandleId(R.id.drag_handle);
		controller.setClickRemoveId(R.id.click_remove);
		controller.setRemoveEnabled(removeEnabled);
		controller.setSortEnabled(sortEnabled);
		controller.setDragInitMode(dragStartMode);
		controller.setRemoveMode(removeMode);

		return controller;
	}
}
