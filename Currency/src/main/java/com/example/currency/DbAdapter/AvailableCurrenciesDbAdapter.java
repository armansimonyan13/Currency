package com.example.currency.DbAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AvailableCurrenciesDbAdapter extends AbstractDbAdapter {

	public static final String DATABASE_TABLE = "available_currencies";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_VALUE = "value";

	static final String QUERY_COUNT = "SELECT COUNT(*) FROM " + DATABASE_TABLE;

	private String[] allColumns = {
			COLUMN_ID,
			COLUMN_NAME,
			COLUMN_VALUE
	};

	public AvailableCurrenciesDbAdapter(Context context) {
		super(context);
	}

	public long insert(String name, double value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, getCount());
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_VALUE, value);

		return database.insert(DATABASE_TABLE, null, values);
	}

	public int delete(long id) {
		return database.delete(DATABASE_TABLE, COLUMN_ID + " = " + id, null);
	}

	public int deleteAll() {
		return database.delete(DATABASE_TABLE, "1", null);
	}

	public Cursor fetch(long id) {
		return database.query(DATABASE_TABLE, allColumns,
				COLUMN_ID + " = " + id,
				null, null, null, null);
	}

	public Cursor fetch(String name) {
		Cursor cursor = database.query(DATABASE_TABLE, allColumns,
				COLUMN_NAME + " = '" + name + "'",
				null, null, null, null);

		cursor.moveToFirst();

		return cursor;
	}

	public Cursor fetchAll() {
		return database.query(DATABASE_TABLE, allColumns,
				null, null, null, null, null);
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

	public int getCount() {
		Cursor cursor = database.rawQuery(QUERY_COUNT, null);
		cursor.moveToFirst();

		int count = cursor.getInt(0);

		cursor.close();

		return count;
	}

	public static RowData cursorToCurrency(Cursor cursor) {
		RowData rowData = null;

		if (cursor.getCount() != 0) {
			rowData = new RowData();

			rowData.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
			rowData.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
			rowData.setValue(cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE)));
		}

		return rowData;
	}

	public static class RowData {
		private long id;
		private String name;
		private double value;

		public RowData() {
		}

		public RowData(long id, String name, double value) {
			this.id = id;
			this.name = name;
			this.value = value;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	}
}
