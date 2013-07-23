package com.example.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesDAO {
	private SQLiteDatabase database;
	private CurrencySQLiteHelper databaseHelper;
	private String[] allColumns = {
			CurrencySQLiteHelper.COLUMN_ID,
			CurrencySQLiteHelper.COLUMN_NAME,
			CurrencySQLiteHelper.COLUMN_VALUE
	};

	public CurrenciesDAO(Context context) {
		databaseHelper = new CurrencySQLiteHelper(context);
	}

	public void open() throws SQLiteException {
		database = databaseHelper.getWritableDatabase();
	}

	public void close() {
		databaseHelper.close();
	}

	public Currency createCurrency(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(CurrencySQLiteHelper.COLUMN_NAME, name);
		values.put(CurrencySQLiteHelper.COLUMN_VALUE, value);

		long insertId = database.insert(CurrencySQLiteHelper.TABLE_CURRENCIES, null, values);
		Cursor cursor = database.query(CurrencySQLiteHelper.TABLE_CURRENCIES,
				allColumns, CurrencySQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Currency newCurrency = cursorToCurrency(cursor);
		cursor.close();
		return newCurrency;
	}

	public Currency getCurrency(String name) {
		Cursor cursor = database.query(CurrencySQLiteHelper.TABLE_CURRENCIES,
				allColumns, CurrencySQLiteHelper.COLUMN_NAME + " = '" + name + "'",
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToCurrency(cursor);
	}

	public int updateCurrency(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(CurrencySQLiteHelper.COLUMN_NAME, name);
		values.put(CurrencySQLiteHelper.COLUMN_VALUE, value);

		return database.update(CurrencySQLiteHelper.TABLE_CURRENCIES, values,
				CurrencySQLiteHelper.COLUMN_NAME + " = '" + name + "'", null);
	}

	public int deleteCurrency(String name) {
		return database.delete(CurrencySQLiteHelper.TABLE_CURRENCIES,
				CurrencySQLiteHelper.COLUMN_NAME + " = '" + name + "'", null);
	}

	public List<Currency> getAllCurrencies() {
		List<Currency> currencies = new ArrayList<Currency>();
		Cursor cursor = database.query(CurrencySQLiteHelper.TABLE_CURRENCIES,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Currency currency = cursorToCurrency(cursor);
			currencies.add(currency);
			cursor.moveToNext();
		}

		cursor.close();
		return currencies;
	}

	private Currency cursorToCurrency(Cursor cursor) {
		Currency currency = new Currency();
		if (cursor.getCount() != 0) {
			currency.setId(cursor.getLong(cursor.getColumnIndex(CurrencySQLiteHelper.COLUMN_ID)));
			currency.setName(cursor.getString(cursor.getColumnIndex(CurrencySQLiteHelper.COLUMN_NAME)));
			currency.setValue(cursor.getDouble(cursor.getColumnIndex(CurrencySQLiteHelper.COLUMN_VALUE)));
		}

		return currency;
	}
}
