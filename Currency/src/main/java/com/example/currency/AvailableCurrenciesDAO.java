package com.example.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

public class AvailableCurrenciesDAO {
	private SQLiteDatabase database;
	private AvailableCurrenciesSQLiteHelper databaseHelper;
	private String[] allColumns = {
			AvailableCurrenciesSQLiteHelper.COLUMN_ID,
			AvailableCurrenciesSQLiteHelper.COLUMN_NAME,
			AvailableCurrenciesSQLiteHelper.COLUMN_VALUE
	};

	public AvailableCurrenciesDAO(Context context) {
		databaseHelper = new AvailableCurrenciesSQLiteHelper(context);
	}

	public void open() throws SQLiteException {
		database = databaseHelper.getWritableDatabase();
	}

	public void close() {
		databaseHelper.close();
	}

	public CurrencyData create(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_NAME, name);
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_VALUE, value);

		long insertId = database.insert(AvailableCurrenciesSQLiteHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, AvailableCurrenciesSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		CurrencyData newCurrency = cursorToCurrency(cursor);
		cursor.close();
		return newCurrency;
	}

	public CurrencyData get(String name) {
		Cursor cursor = database.query(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, AvailableCurrenciesSQLiteHelper.COLUMN_NAME + " = '" + name + "'",
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToCurrency(cursor);
	}

	public CurrencyData get(int id) {
		Cursor cursor = database.query(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, AvailableCurrenciesSQLiteHelper.COLUMN_ID + " = " + id,
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToCurrency(cursor);
	}

	public int update(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_NAME, name);
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_VALUE, value);

		return database.update(AvailableCurrenciesSQLiteHelper.TABLE_NAME, values,
				AvailableCurrenciesSQLiteHelper.COLUMN_NAME + " = '" + name + "'", null);
	}

	public int update(long id, String name, double value) {
		ContentValues values = new ContentValues();
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_NAME, name);
		values.put(AvailableCurrenciesSQLiteHelper.COLUMN_VALUE, value);

		return database.update(AvailableCurrenciesSQLiteHelper.TABLE_NAME, values,
				AvailableCurrenciesSQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public int delete(String name) {
		return database.delete(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				AvailableCurrenciesSQLiteHelper.COLUMN_NAME + " = '" + name + "'", null);
	}

	public List<CurrencyData> getAll() {
		List<CurrencyData> currencies = new ArrayList<CurrencyData>();
		Cursor cursor = database.query(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CurrencyData currency = cursorToCurrency(cursor);
			currencies.add(currency);
			cursor.moveToNext();
		}

		cursor.close();
		return currencies;
	}

	public Cursor getCursor() {
		return database.query(AvailableCurrenciesSQLiteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
	}

	private CurrencyData cursorToCurrency(Cursor cursor) {
		CurrencyData currency = new CurrencyData();
		if (cursor.getCount() != 0) {
			currency.setId(cursor.getLong(cursor.getColumnIndex(AvailableCurrenciesSQLiteHelper.COLUMN_ID)));
			currency.setName(cursor.getString(cursor.getColumnIndex(AvailableCurrenciesSQLiteHelper.COLUMN_NAME)));
			currency.setValue(cursor.getDouble(cursor.getColumnIndex(AvailableCurrenciesSQLiteHelper.COLUMN_VALUE)));
		}

		return currency;
	}

	public void move(int from, int to) {
		CurrencyData fromCurrency = get(from);
		CurrencyData toCurrency = get(to);

		update(fromCurrency.getId(), toCurrency.getName(), toCurrency.getValue());
		update(toCurrency.getId(), fromCurrency.getName(), fromCurrency.getValue());
	}

	public void deleteAll() {
		database.delete(AvailableCurrenciesSQLiteHelper.TABLE_NAME, null, null);
	}
}
