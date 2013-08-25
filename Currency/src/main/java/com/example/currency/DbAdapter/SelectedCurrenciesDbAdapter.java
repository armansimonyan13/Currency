package com.example.currency.DbAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class SelectedCurrenciesDbAdapter extends AbstractDbAdapter {

	static final String DATABASE_TABLE = "selected_currencies";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "currency_name";

	static final String QUERY_COUNT = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
	static final String QUERY_JOINED = "SELECT " + DATABASE_TABLE + "." + COLUMN_ID
			+ ", " + AvailableCurrenciesDbAdapter.COLUMN_NAME
			+ ", " + AvailableCurrenciesDbAdapter.COLUMN_VALUE
			+ " FROM " + DATABASE_TABLE
			+ " JOIN " + AvailableCurrenciesDbAdapter.DATABASE_TABLE
			+ " ON " + DATABASE_TABLE + "." + COLUMN_NAME + " = "
			+ AvailableCurrenciesDbAdapter.DATABASE_TABLE + "." + AvailableCurrenciesDbAdapter.COLUMN_NAME + ";";

	private String[] allColumns = {
			COLUMN_ID,
			COLUMN_NAME
	};

	public SelectedCurrenciesDbAdapter(Context context) {
		super(context);
	}

	public long insert(String name) {
		return insert(getCount(), name);
	}

	public long insert(long id, String name) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, id);
		values.put(COLUMN_NAME, name);

		return database.insert(DATABASE_TABLE, null, values);
	}

	public int update(long id, String name) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, id);
		values.put(COLUMN_NAME, name);

		return database.update(DATABASE_TABLE, values, COLUMN_ID + " = " + id, null);
	}

	public Cursor fetch(long id) {
		return database.query(DATABASE_TABLE, allColumns, COLUMN_ID + " = " + id,
				null, null, null, null);
	}

	public Cursor fetchAll() {
		return database.query(DATABASE_TABLE, allColumns, null, null, null, null, null);
	}

	public Cursor fetchAllJoined() {
		return database.rawQuery(QUERY_JOINED, null);
	}

	public int delete(int id) {
		List<RowData> rowDataList = new ArrayList<RowData>();

		Cursor cursor = fetchAll();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			RowData rowData = cursorToCurrency(cursor);

			if (rowData.getId() != id) {
				rowDataList.add(rowData);
			}

			cursor.moveToNext();
		}

		cursor.close();

		deleteAll();

		for (RowData rowData : rowDataList) {
			insert(rowData.getName());
		}

		return 1;
	}

	public int delete(String name) {
		List<RowData> rowDataList = new ArrayList<RowData>();

		Cursor cursor = fetchAll();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			RowData rowData = cursorToCurrency(cursor);

			if (!name.equals(rowData.getName())) {
				rowDataList.add(rowData);
			}

			cursor.moveToNext();
		}

		cursor.close();

		deleteAll();

		for (RowData rowData : rowDataList) {
			insert(rowData.getName());
		}

		return 1;
	}

	public int deleteAll() {
		return database.delete(DATABASE_TABLE, "1", null);
	}

	public boolean isEmpty() {
		if (getCount() == 0) {
			return true;
		}

		return false;
	}

	public boolean isOpen() {
		return database.isOpen();
	}

	public int getCount() {
		Cursor cursor = database.rawQuery(QUERY_COUNT, null);
		cursor.moveToFirst();

		int count = cursor.getInt(0);

		cursor.close();

		return count;
	}

	public void replace(long from, long to) {
		List<RowData> rowDataList = new ArrayList<RowData>();
		RowData fromRowData = null;

		Cursor cursor = fetchAll();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			RowData rowData = cursorToCurrency(cursor);

			if (rowData.getId() != from) {
				rowDataList.add(rowData);
			} else {
				fromRowData = rowData;
			}

			cursor.moveToNext();
		}

		cursor.close();

		rowDataList.add((int) to, fromRowData);

		deleteAll();

		for (RowData rowData : rowDataList) {
			insert(rowData.getName());
		}
	}

	public boolean contains(String name) {
		Cursor cursor = database.query(DATABASE_TABLE, allColumns,
				COLUMN_NAME + " = '" + name + "'",
				null, null, null, null);

		int count = cursor.getCount();

		cursor.close();

		if (count == 0) {
			return false;
		}

		return true;
	}

	public static RowData cursorToCurrency(Cursor cursor) {
		RowData rowData = new RowData();

		rowData.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		rowData.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));

		return rowData;
	}

	public static class RowData {
		private long id;
		private String name;

		public RowData() {
		}

		public RowData(long id, String name) {
			this.id = id;
			this.name = name;
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
	}
}
