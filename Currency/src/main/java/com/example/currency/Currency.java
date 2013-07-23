package com.example.currency;

public class Currency {
	private long id;
	private String name;
	private double value;

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
