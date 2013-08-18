package com.example.currency;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AbstractDbAdapter {
	protected DatabaseHelper databaseHelper;
	protected SQLiteDatabase database;

	protected static final String TABLE_CREATE_AVAILABLE_CURRENCIES = "create table "
			+ AvailableCurrenciesDbAdapter.DATABASE_TABLE + "("
			+ AvailableCurrenciesDbAdapter.COLUMN_ID + " integer primary key autoincrement, "
			+ AvailableCurrenciesDbAdapter.COLUMN_NAME + " text not null, "
			+ AvailableCurrenciesDbAdapter.COLUMN_VALUE + " real);";

	protected static final String TABLE_CREATE_SELECTED_CURRENCIES = "create table "
			+ SelectedCurrenciesDbAdapter.DATABASE_TABLE + "("
			+ SelectedCurrenciesDbAdapter.COLUMN_ID + " integer primary key, "
			+ SelectedCurrenciesDbAdapter.COLUMN_CURRENCY_ID + " integer);";

	protected static final String DROP_TABLE_AVAILABLE_CURRENCIES = "DROP TABLE IF EXISTS "
			+ AvailableCurrenciesDbAdapter.DATABASE_TABLE;
	protected static final String DROP_TABLE_SELECTED_CURRENCIES = "DROP TABLE IF EXISTS "
			+ SelectedCurrenciesDbAdapter.DATABASE_TABLE;

	protected static final String DATABASE_NAME = "currency.db";
	protected static final int DATABASE_VERSION = 1;

	protected final Context context;

	protected static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase) {
			sqLiteDatabase.execSQL(TABLE_CREATE_AVAILABLE_CURRENCIES);
			sqLiteDatabase.execSQL(TABLE_CREATE_AVAILABLE_CURRENCIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
			sqLiteDatabase.execSQL(DROP_TABLE_AVAILABLE_CURRENCIES);
			sqLiteDatabase.execSQL(DROP_TABLE_SELECTED_CURRENCIES);

			onCreate(sqLiteDatabase);
		}
	}

	public AbstractDbAdapter(Context context) {
		this.context = context;
	}

	public AbstractDbAdapter open() throws SQLException {
		databaseHelper = new DatabaseHelper(context);
		database = databaseHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		databaseHelper.close();
	}
}
