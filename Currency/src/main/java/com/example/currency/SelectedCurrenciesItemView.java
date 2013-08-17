package com.example.currency;

import android.widget.ImageView;

public class SelectedCurrenciesItemView {
	private ImageView flag;
	private String name;
	private Double value;

	public void setFlag(ImageView flag) {
		this.flag = flag;
	}

	public ImageView getFlag() {
		return flag;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}
}
