package com.example.currency;

public class CurrencyData {
	private long id;
	private String name;
	private double value;

	public CurrencyData() {
	}

	public CurrencyData(long id, String name, double value) {
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
