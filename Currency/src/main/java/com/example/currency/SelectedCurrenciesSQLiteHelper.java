package com.example.currency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SelectedCurrenciesSQLiteHelper extends SQLiteOpenHelper {
	static final String TABLE_NAME = "selected_currencies";
	static final String COLUMN_ID = "_id";
	static final String COLUMN_CURRENCY_ID = "currency_id";

	static final String DATABASE_NAME = "currency.db";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME + "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_CURRENCY_ID + " integer);";

	static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

	public SelectedCurrenciesSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL(DROP_TABLE);
		onCreate(sqLiteDatabase);
	}
}
