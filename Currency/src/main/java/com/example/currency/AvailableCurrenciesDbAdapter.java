package com.example.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

public class AvailableCurrenciesDbAdapter extends AbstractDbAdapter {
	public static final String DATABASE_TABLE = "available_currencies";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_VALUE = "value";

	private String[] allColumns = {
			COLUMN_ID,
			COLUMN_NAME,
			COLUMN_VALUE
	};

	public AvailableCurrenciesDbAdapter(Context context) {
		super(context);
	}

	public long create(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_VALUE, value);

		return database.insert(DATABASE_TABLE, null, values);
	}

	public int delete(long id) {
		return database.delete(DATABASE_TABLE, COLUMN_ID + " = " + id, null);
	}

	public void deleteAll() {
		database.delete(DATABASE_TABLE, null, null);
	}

	public Cursor fetchAll() {
		return database.query(DATABASE_TABLE, allColumns,
				null, null, null, null, null);
	}

	public Cursor fetch(long id) {
		return database.query(DATABASE_TABLE, allColumns,
				COLUMN_ID + " = " + id,
				null, null, null, null);
	}

	public Cursor fetch(String name) {
		return database.query(DATABASE_TABLE, allColumns,
				COLUMN_NAME + " = '" + name + "'",
				null, null, null, null);
	}

	public int update(String name, double value) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_VALUE, value);

		return database.update(DATABASE_TABLE, values,
				COLUMN_NAME + " = '" + name + "'", null);
	}

	public int update(long id, String name, double value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_VALUE, value);

		return database.update(DATABASE_TABLE, values,
				COLUMN_ID + " = " + id, null);
	}

	public static CurrencyData cursorToCurrency(Cursor cursor) {
		CurrencyData currency = new CurrencyData();
		if (cursor.getCount() != 0) {
			currency.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
			currency.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
			currency.setValue(cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE)));
		}

		return currency;
	}

	public void replace(int from, int to) {
		CurrencyData fromCurrency = cursorToCurrency(fetch(from));
		CurrencyData toCurrency = cursorToCurrency(fetch(to));

		update(fromCurrency.getId(), toCurrency.getName(), toCurrency.getValue());
		update(toCurrency.getId(), fromCurrency.getName(), fromCurrency.getValue());
	}
}
