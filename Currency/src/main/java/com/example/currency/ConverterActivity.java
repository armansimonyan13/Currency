package com.example.currency;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class ConverterActivity extends Activity {
	public static final String FROM_FLAG = "from_flag";
	public static final String TO_FLAG = "to_flag";
	public static final String TO_VALUE = "to_value";

	private double coefficient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_converter);

		double toValue = getIntent().getDoubleExtra(TO_VALUE, 0.0);
		coefficient = toValue;
		EditText toCount = (EditText) findViewById(R.id.to_value);
		toCount.setText(String.valueOf(toValue));

		String currencyName = getIntent().getStringExtra(TO_FLAG);
		ImageView toFlagImageView = (ImageView) findViewById(R.id.to_flag);
		int toFlagImageResourceId = getResources().getIdentifier("flag_" + currencyName, "drawable", getPackageName());
		toFlagImageView.setImageResource(toFlagImageResourceId);

		final EditText fromValueEditText = (EditText) findViewById(R.id.from_value);
		final EditText toValueEditText = (EditText) findViewById(R.id.to_value);

		fromValueEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

			@Override
			public void afterTextChanged(Editable editable) {
				if (fromValueEditText.hasFocus()) {
					Double value;
					try {
						value = Double.valueOf(fromValueEditText.getText().toString());
					} catch (NumberFormatException e) {
						value = 0.0;
					}
					toValueEditText.setText(String.format("%1$.4f", value * coefficient));
				}
			}
		});

		toValueEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

			@Override
			public void afterTextChanged(Editable editable) {
				if (toValueEditText.hasFocus()) {
					Double value;
					try {
						value = Double.valueOf(toValueEditText.getText().toString());
					} catch (NumberFormatException e) {
						value = 0.0;
					}
					fromValueEditText.setText(String.format("%1$.4f", value / coefficient));
				}
			}
		});

		DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
				TextView textView = (TextView) ConverterActivity.this.findViewById(R.id.text_view);
				textView.setText(year + "/" + (month + 1) + "/" + day);
			}
		});
	}
}
