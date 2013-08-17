package com.example.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class SelectedCurrenciesDAO {
	static final String QUERY_COUNT = "SELECT COUNT(*) FROM " + SelectedCurrenciesSQLiteHelper.TABLE_NAME;
	static final String QUERY_JOINED = "SELECT " + SelectedCurrenciesSQLiteHelper.COLUMN_ID
			+ ", " + AvailableCurrenciesSQLiteHelper.COLUMN_NAME
			+ ", " + AvailableCurrenciesSQLiteHelper.COLUMN_VALUE
			+ " FROM " + SelectedCurrenciesSQLiteHelper.TABLE_NAME
			+ " JOIN " + AvailableCurrenciesSQLiteHelper.TABLE_NAME
			+ " ON " + SelectedCurrenciesSQLiteHelper.TABLE_NAME + "." + SelectedCurrenciesSQLiteHelper.COLUMN_CURRENCY_ID + " = "
			+ AvailableCurrenciesSQLiteHelper.TABLE_NAME + "." + AvailableCurrenciesSQLiteHelper.COLUMN_ID + ";";

	private SQLiteDatabase database;
	private SelectedCurrenciesSQLiteHelper databaseHelper;

	private String[] allColumns = {
			SelectedCurrenciesSQLiteHelper.COLUMN_ID,
			SelectedCurrenciesSQLiteHelper.COLUMN_CURRENCY_ID
	};

	public SelectedCurrenciesDAO(Context context) {
		databaseHelper = new SelectedCurrenciesSQLiteHelper(context);
	}

	public void open() throws SQLiteException {
		database = databaseHelper.getWritableDatabase();
	}

	public void close() {
		databaseHelper.close();
	}

	public boolean isEmpty() {
		Cursor cursor = database.rawQuery(QUERY_COUNT, null);
		cursor.moveToFirst();

		if (cursor.getInt(0) == 0) {
			return true;
		}

		return false;
	}

	public Cursor getCursor() {
		return database.query(SelectedCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
	}

	public Cursor getJoinedCursor() {
		return database.rawQuery(QUERY_JOINED, null);
	}

	public long insert(long currencyId) {
		ContentValues values = new ContentValues();
		values.put(SelectedCurrenciesSQLiteHelper.COLUMN_CURRENCY_ID, currencyId);

		return database.insert(AvailableCurrenciesSQLiteHelper.TABLE_NAME, null, values);
	}

	public long update(long id, long currencyId) {
		ContentValues values = new ContentValues();
		values.put(SelectedCurrenciesSQLiteHelper.COLUMN_ID, id);
		values.put(SelectedCurrenciesSQLiteHelper.COLUMN_CURRENCY_ID, currencyId);

		return database.update(SelectedCurrenciesSQLiteHelper.TABLE_NAME, values,
				AvailableCurrenciesSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
}
