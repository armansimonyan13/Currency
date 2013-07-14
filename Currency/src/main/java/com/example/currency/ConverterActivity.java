package com.example.currency;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class ConverterActivity extends Activity {
	public static final String TO_VALUE = "to_value";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_converter);

		double toValue = getIntent().getDoubleExtra(TO_VALUE, 0.0);
		EditText toCount = (EditText) findViewById(R.id.to_count);
		toCount.setText(String.valueOf(toValue));
		Log.d("toCount", String.valueOf(toValue));
	}
}
