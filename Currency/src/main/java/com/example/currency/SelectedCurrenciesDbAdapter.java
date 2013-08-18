package com.example.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SelectedCurrenciesDbAdapter extends AbstractDbAdapter {

	static final String DATABASE_TABLE = "selected_currencies";
	static final String COLUMN_ID = "_id";
	static final String COLUMN_CURRENCY_ID = "currency_id";

	static final String QUERY_COUNT = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
	static final String QUERY_JOINED = "SELECT " + COLUMN_ID
			+ ", " + AvailableCurrenciesDbAdapter.COLUMN_NAME
			+ ", " + AvailableCurrenciesDbAdapter.COLUMN_VALUE
			+ " FROM " + DATABASE_TABLE
			+ " JOIN " + AvailableCurrenciesDbAdapter.DATABASE_TABLE
			+ " ON " + DATABASE_TABLE + "." + COLUMN_CURRENCY_ID + " = "
			+ AvailableCurrenciesDbAdapter.DATABASE_TABLE + "." + AvailableCurrenciesDbAdapter.COLUMN_ID + ";";

	private String[] allColumns = {
			COLUMN_ID,
			COLUMN_CURRENCY_ID
	};

	public SelectedCurrenciesDbAdapter(Context context) {
		super(context);
	}

	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}

		return false;
	}

	public int size() {
		Cursor cursor = database.rawQuery(QUERY_COUNT, null);
		cursor.moveToFirst();

		return cursor.getInt(0);
	}

	public Cursor fetchAll() {
		return database.query(DATABASE_TABLE, allColumns, null, null, null, null, null);
	}

	public Cursor fetchAllJoined() {
		return database.rawQuery(QUERY_JOINED, null);
	}

	public long insert(long currencyId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, size());
		values.put(COLUMN_CURRENCY_ID, currencyId);

		return database.insert(DATABASE_TABLE, null, values);
	}

	public int update(long id, long currencyId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, id);
		values.put(COLUMN_CURRENCY_ID, currencyId);

		return database.update(DATABASE_TABLE, values, COLUMN_ID + " = " + id, null);
	}

	public Cursor fetch(long id) {
		 return database.query(DATABASE_TABLE, allColumns, COLUMN_ID + " = " + id,
				null, null, null, null);
	}

	public void replace(long from, long to) {
		long fromValue = fetch(from).getLong(1);
		long toValue = fetch(to).getLong(1);

		update(from, toValue);
		update(to, fromValue);
	}

	public int delete(int id) {
		return database.delete(DATABASE_TABLE, COLUMN_ID + " = " + id, null);
	}

	public boolean contains(int currencyId) {
		Cursor cursor = database.query(DATABASE_TABLE, allColumns,
				COLUMN_CURRENCY_ID + " = " + currencyId,
				null, null, null, null);

		if (cursor.getCount() == 0) {
			return false;
		}

		return true;
	}
}
